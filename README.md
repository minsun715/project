

1. First, I assumed that we use over Oracle 10g database environment.

2. I create table schema for managing Curse Words below.


create table CURSEWORDS (
    text varchar(100) 
   , use_yn varchar(1) default '1'
)
;

insert into CURSEWORDS (text, use_yn) values ('FUCK', '1');
insert into CURSEWORDS (text, use_yn) values ('fuck', '1');
insert into CURSEWORDS (text, use_yn) values ('asshole', '0');
insert into CURSEWORDS (text, use_yn) values ('BitCh', '1');

3. Then, I use this query to check if the comment includes any of the curse words.

select *
  from curseWordsList
 where exists (
   select 'x'
     from dual
    where regexp_like(replace(lower('Hello Thi F UC KassholE'), ' ', ''), text)
   )
   and use_yn = '1'
;


4. The source codes files I modified are below.
   - de/hybris/platform/customerreview/jalo/CustomerReviewTestMinsun.java   // testCrateCustomerReviews()
   - de/hybris/platform/customerreview/jalo/CustomerReviewManager.java      // getNumberOfCurseWords()
   - de/hybris/platform/customerreview/impl/DefaultCustomerReviewService.java  // getNumberOfCurseWords()
   - de/hybris/platform/customerreview/CustomerReviewService.java     // getNumberOfCurseWords()


