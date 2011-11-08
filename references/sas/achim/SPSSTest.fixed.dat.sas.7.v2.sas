/* =====================
   DExT Script Generator
   =====================

   Target syntax   : SAS
   ===================== */


/* **** configuration start */

%let raw_data = 'E:\DDI\UKDA\DExT\Pascal\2008-02-22\SPSSTest.fixed.dat' ; /* with quotes */
%let sas_file_dirname = 'E:\DDI\UKDA\DExT\Pascal\2008-02-22' ; /* with quotes */
%let sas_file_basename = data ; /* without quotes */
options locale = en_US ; /* only necessary if file contains internationalization and localization specific data */

/* **** configuration end */

filename raw_data &raw_data ;
libname library &sas_file_dirname ;
/* options fmtsearch = ( library.&sas_file_basename ) ; */

/* remove possible existing data and catalog files */
proc datasets library = library ;
    delete &sas_file_basename formats sasmacr / memtype = all ;
run;

proc format library = library ;
/* NUMERIC */
    value V1_
        1.00=" [missing]"
        2.00=" [missing]"
        3.00=" [missing]"
    ;

/* NUMER16 */
    value V2_
        1.00=" [missing]"
        2.00="Deux manquant [missing]"
        3.00=" [missing]"
        4.00=" [missing]"
        5.00=" [missing]"
    ;

/* NUMER16B */
    value V3_
        1=" [missing]"
        2=" [missing]"
        3="Trois manquant [missing]"
        4=" [missing]"
        5=" [missing]"
        10=" [missing]"
    ;

/* NUMER17 */
    value V4_
        99.00="Sysmiss [missing]"
        -1.00=" [missing]"
        1.00="One"
        2.00="Two"
        3.00="Three"
    ;

/* SCIENT02 */
    value V9_
        1.0E+00="One"
        1.5E+00="One point five"
        1.0E+09="One to the 10"
    ;

/* STR8 */
    value $V43_
        "A"=" [missing]"
        "B"=" [missing]"
        "MMM"="M comme manquant [missing]"
        "X"="Icks"
        "ZZZ"="Zeeeess"
    ;

/* YESNOMAY */
    value V45_
        0="No"
        1="Yes"
        2="Maybe"
    ;
run;

data library.&sas_file_basename ;

    infile raw_data LRECL=725;

    input
        @1      NUMERIC     8.2
        @9      NUMER16     16.2
        @25     NUMER16B    16.0
        @41     NUMER17     17.2
        @58     NUMER32     32.2
        @90     COMMA       NLNUM8.2
        @98     DOT         NLNUM8.2
        @106    SCIENT01    8.2
        @114    SCIENT02    16.2
        @130    SCIENTB2    15.2
        @145    SCIENTC2    17.2
        @162    SCIENT03    10.4
        @172    SCIENT04    8.2
        @180    DATE01      DATE11.
        @191    DATE02      DATE9.
        @200    DATE03      MMDDYY10.
        @210    DATE04      MMDDYY8.
        @218    DATE05      DDMMYY10.
        @228    DATE06      DDMMYY8.
        @236    DATE07      JULIAN7.
        @243    DATE08      JULIAN5.
        @248    DATE09      $CHAR8.
        @256    DATE10      $CHAR6.
        @262    DATE11      MONYY8.
        @270    DATE12      MONYY6.
        @276    DATE13      $CHAR10.
        @286    DATE14      $CHAR8.
        @294    DATE15      DATETIME17.
        @311    DATE16      DATETIME20.
        @331    DATE17      DATETIME23.
        @354    DATE18      TIME5.
        @359    DATE19      TIME8.
        @367    DATE20      TIME11.
        @378    DATE21      $CHAR9.
        @387    DATE22      $CHAR12.
        @399    DATE23      $CHAR15.
        @414    DATE24      $CHAR9.
        @423    DATE25      $CHAR3.
        @426    DATE26      $CHAR3.
        @429    DOLLAR      COMMA15.2
        @444    CUSTOMA     COMMA8.2
        @452    CUSTOMB     COMMA5.0
        @457    STR8        $CHAR8.
        @465    STR250      $CHAR250.
        @715    YESNOMAY    1.0
        @716    WEIGHT1     10.0
    ;

	label
        NUMERIC = "Numeric variable"
        NUMER16 = "Numeric 16.2"
        NUMER16B = "Numeric 16.0"
        NUMER17 = "Numeric 17.2"
        NUMER32 = "Numeric 32.2"
        COMMA = "Comma variable"
        DOT = "Dot variable"
        SCIENT01 = "Scientific 8.2"
        SCIENT02 = "Scientific 16.2"
        SCIENTB2 = "Scientific 15.2"
        SCIENTC2 = "Scientific 17.2"
        SCIENT03 = "Scientific 10.4"
        SCIENT04 = "Scientific 8.0"
        DATE01 = "Date dd-mmm-yyyy"
        DATE02 = "Date dd-mmm-yy"
        DATE03 = "Date mm/dd/yyyy"
        DATE04 = "Date mm/dd/yy"
        DATE05 = "Date dd.mm.yyyy"
        DATE06 = "Date dd.mm.yy"
        DATE07 = "Date yyyyddd"
        DATE08 = "Date yyddd"
        DATE09 = "Date q Q yyyy"
        DATE10 = "Date q Q yy"
        DATE11 = "Date mmm yyyy"
        DATE12 = "Date mmm yy"
        DATE13 = "Date ww WK yyyy"
        DATE14 = "Date ww WK yy"
        DATE15 = "Date dd-mmm-yyyy hh:mm"
        DATE16 = "Date dd-mmm-yyyy hh:mm:ss"
        DATE17 = "Date dd-mmn-yyyy hh:mm:ss.ss"
        DATE18 = "Date hh:mm"
        DATE19 = "Date hh:mm:ss"
        DATE20 = "Date hh:mm:ss.ss"
        DATE21 = "Date ddd:hh:mm"
        DATE22 = "Date ddd:hh:mm:ss"
        DATE23 = "Date ddd:hh:mm:ss.ss"
        DATE24 = "Date Monday, Tuesday,..."
        DATE25 = "Date Mon,Tue,Wed,..."
        DATE26 = "Date Jan,Feb,Mar,..."
        DOLLAR = "Dollar variable"
        CUSTOMA = "Custom currency A"
        CUSTOMB = "Custom currency B"
        STR8 = "String variable (8)"
        STR250 = "String variable (250)"
        YESNOMAY = "Yes/No/Maybe"
        WEIGHT1 = "Weight variable"
	;

	/* associate permanently SAS output formats */
	format
        NUMERIC     8.2
        NUMER16     16.2
        NUMER16B    16.0
        NUMER17     17.2
        NUMER32     32.2
        COMMA       NLNUM8.2
        DOT         NLNUM8.2
        SCIENT01    8.2
        SCIENT02    16.2
        SCIENTB2    15.2
        SCIENTC2    17.2
        SCIENT03    10.4
        SCIENT04    8.2
        DATE01      DATE9.
        DATE02      DATE9.
        DATE03      MMDDYY10.
        DATE04      MMDDYY8.
        DATE05      DDMMYY10.
        DATE06      DDMMYY8.
        DATE07      JULIAN7.
        DATE08      JULIAN5.
        DATE09      $CHAR8.
        DATE10      $CHAR6.
        DATE11      MONYY7.
        DATE12      MONYY6.
        DATE13      $CHAR10.
        DATE14      $CHAR8.
        DATE15      DATETIME17.
        DATE16      DATETIME20.
        DATE17      DATETIME23.
        DATE18      TIME5.
        DATE19      TIME8.
        DATE20      TIME11.
        DATE21      $CHAR9.
        DATE22      $CHAR12.
        DATE23      $CHAR15.
        DATE24      $CHAR9.
        DATE25      $CHAR3.
        DATE26      $CHAR3.
        DOLLAR      COMMA15.2
        CUSTOMA     COMMA8.2
        CUSTOMB     COMMA5.0
        STR8        $CHAR8.
        STR250      $CHAR250.
        YESNOMAY    1.0
        WEIGHT1     10.0
    ;

run;

options mstored sasmstore = library ;

%macro associate_user_defined_formats / store des = 'associates user-defined formats' ;

	format
        NUMERIC V1_.
        NUMER16 V2_.
        NUMER16B V3_.
        NUMER17 V4_.
        SCIENT02 V9_.
        STR8 $V43_.
        YESNOMAY V45_.
	;

%mend associate_user_defined_formats ;
run;
