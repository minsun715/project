/*     */ package de.hybris.platform.customerreview.jalo;
/*     */ 
/*     */ import de.hybris.platform.customerreview.constants.CustomerReviewConstants;
/*     */ import de.hybris.platform.jalo.ConsistencyCheckException;
/*     */ import de.hybris.platform.jalo.JaloInvalidParameterException;
/*     */ import de.hybris.platform.jalo.JaloSession;
/*     */ import de.hybris.platform.jalo.SessionContext;
/*     */ import de.hybris.platform.jalo.product.Product;
/*     */ import de.hybris.platform.jalo.product.ProductManager;
/*     */ import de.hybris.platform.jalo.user.Customer;
/*     */ import de.hybris.platform.jalo.user.User;
/*     */ import de.hybris.platform.jalo.user.UserGroup;
/*     */ import de.hybris.platform.jalo.user.UserManager;
/*     */ import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
/*     */ import de.hybris.platform.util.Config;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.junit.After;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Before;
/*     */ import org.junit.Test;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomerReviewTest
/*     */   extends HybrisJUnit4TransactionalTest
/*     */ {
/*     */   private static final Logger LOG = Logger.getLogger(CustomerReviewTest.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String headline = "Test headline";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String comment = "Text text text";
/*     */   
/*     */ 
/*     */ 
/*     */   private final List<Customer> demoUsers = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   private Product demoProduct = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private Double ratingLegal = null;
/*     */   private String oldMinimalRating;
/*     */   private String oldMaximalRating;
/*     */   
/*     */   @Before
/*     */   public void setUp()
/*     */     throws ConsistencyCheckException
/*     */   {
/*     */     this.demoProduct = ProductManager.getInstance().createProduct("demo1");
/*     */     
/*     */ 
/*     */ 
/*     */     Customer demo22 = UserManager.getInstance().createCustomer("demo22");
/*     */     Customer demo3 = UserManager.getInstance().createCustomer("demo3");
/*     */     Customer demo4 = UserManager.getInstance().createCustomer("demo4");
/*     */     
/*     */     this.demoUsers.add(demo22);
/*     */     this.demoUsers.add(demo3);
/*     */     this.demoUsers.add(demo4);
/*     */     
/*     */     this.oldMinimalRating = Config.getParameter("customerreview.minimalrating");
/*     */     this.oldMaximalRating = Config.getParameter("customerreview.maximalrating");
/*     */     
/*     */ 
/*     */     Config.setParameter("customerreview.minimalrating", String.valueOf(2));
/*     */     Config.setParameter("customerreview.maximalrating", String.valueOf(4));
/*     */     this.ratingLegal = new Double(3.0D);
/*     */   }
/*     */   
/*     */   @After
/*     */   public void tearDown()
/*     */   {
/*     */     Config.setParameter("customerreview.minimalrating", this.oldMinimalRating);
/*     */     Config.setParameter("customerreview.maximalrating", this.oldMaximalRating);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private CustomerReview createCR(Double rating)
/*     */   {
/*     */     return createCR(rating, (User)this.demoUsers.get(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private CustomerReview createCR(Double rating, User user)
/*     */   {
/*     */     return CustomerReviewManager.getInstance().createCustomerReview(rating, "Test headline", "Text text text", user, this.demoProduct);
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testCorrectSetting()
/*     */   {
/*     */     CustomerReview cr = createCR(this.ratingLegal);
/*     */     Assert.assertEquals(this.ratingLegal, cr.getRating());
/*     */     Assert.assertEquals("Test headline", cr.getHeadline());
/*     */     Assert.assertEquals("Text text text", cr.getComment());
/*     */     cr.setRating(CustomerReviewConstants.getInstance().MAXRATING);
/*     */     Assert.assertEquals(new Double(CustomerReviewConstants.getInstance().MAXRATING), cr.getRating());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Test
/*     */   public void testCrateCustomerReviews()
/*     */   {
/*     */     Config.setParameter("customerreview.minimalrating", String.valueOf(1));
/*     */     Config.setParameter("customerreview.maximalrating", String.valueOf(10));

/*     */     createCR(new Double(2.0D), (User)this.demoUsers.get(0));
/*     */     createCR(new Double(2.0D), (User)this.demoUsers.get(1));
/*     */     createCR(new Double(3.0D), (User)this.demoUsers.get(2));


/*     */     CustomerReview newReview = createCR(this.ratingLegal);
              Double inputRating = new Double(3.0D);
              String content = “Test Content”;


              /* Check if the rating is not < 0  */
              try
/*     */     {
/*     */       newReview.setRating(inputRating);
/*     */       Assert.fail("JaloInvalidParameterException expected");
/*     */     }
/*     */     catch (JaloInvalidParameterException localJaloInvalidParameterException) {}


              /* Check if Customer’s comment contains any of the curse words */
              Integer executedRtn = CustomerReviewManager.getInstance().getNumberOfCurseWords(ctx, content);
              
              if (executedRtn.intValue() > 0)
              {
       		Assert.fail("getNumberOfCurseWords is nonsense as curse words in list");
    	      }
              Assert.assertEquals(executedRtn.intValue(), 0);


              /*Create Customer Review*/
              newReview = CustomerReviewManager.getInstance().createCustomerReview(inputRating, "Test headline", content,
                         (User)this.demoUsers.get(1), this.demoProduct);

/*     */     SessionContext ctx = CustomerReviewManager.getInstance().getSession().getSessionContext();

              Integer totalReviewCount = CustomerReviewManager.getInstance().getNumberOfReviews(ctx, this.demoProduct);
              
              if (totalReviewCount.intValue() < 1)
              {
       		Assert.fail("testCrateCustomerReviews is nonsense as no review in list");
    	      }

/*     */     List<CustomerReview> reviewList = CustomerReviewManager.getInstance().getAllReviews(ctx, this.demoProduct);
/*     */     Assert.assertEquals(totalReviewCount.intValue(), reviewList.size());
/*     */   }
/*     */ 
/*     */ }


/* Location:              /Users/TJL4646/CustomerReview_Assignment/customerreviewserver.jar!/de/hybris/platform/customerreview/jalo/CustomerReviewTest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */