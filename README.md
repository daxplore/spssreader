# SPSS Reader #

A Java library that reads SPSS .sav files.

Original project:
----------------

This is a fork of an Open Data Foundation project created by Pascal Heus (pheus@opendatafoundation.org).

The original project can be found at: http://opendatafoundation.org/?lvl1=forge


Major changes:
-------------

* We have added an optional charset argument to the SPSSFile constructors. This makes it possible to read SPSS files that don't use the Java platform's default charset.


Minor changes:
-------------

* We have made it possible to turn off logging, by making the logFlag in SPSSFile public.
* We have commented out some code that prevented us from getting a variable's long name.
* Updated the xmlns:xsl link in xml2html.xslt.
* We have cleaned up and formatted the code to some extent, according to git and Java conventions.


License:
-------

GNU Lesser General Public License

