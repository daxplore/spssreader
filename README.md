This is a Java library that can be used for reading SPSS .sav files.


__Original project:__
This is a fork of an Open Data Foundation project created by Pascal Heus (pheus@opendatafoundation.org). The original project can be found at: http://opendatafoundation.org/?lvl1=forge


__Major changes:__
* We have added an optional charset argument to the SPSSFile constructors.
This makes it possible to read SPSS files that do not use the platform's default charset.


__Minor changes:__
* We have made it possible to turn off logging, by making the logFlag in SPSSFile public.
* We have commented out some code that prevented us from getting a variable's long name.
* We removed a div-tag in xml2html.xslt that caused errors in Eclipse.


__License:__
GNU Lesser General Public License

