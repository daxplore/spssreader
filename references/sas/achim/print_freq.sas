%let sas_file_dirname = 'E:\DDI\UKDA\DExT\Pascal\2008-02-22' ; /* with quotes */
%let sas_file_basename = data ; /* without quotes */

libname library &sas_file_dirname ;
options mstored sasmstore=library _last_=library.&sas_file_basename ;

proc print ;
	/* associates temporarily user-defined formats from catalog */
	%associate_user_defined_formats
run ;

proc freq ;
	/* associates temporarily user-defined formats from catalog */
	%associate_user_defined_formats
run ;

