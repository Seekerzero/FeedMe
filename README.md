UI 
-----------------------
- Awards gallery (SL)
- Fragment for awards description (SL)
- Get and import all mochis 
- Get and import backgrounds
- Get and import icons 
- Instructions page (SL) 
- Animation?? (last last step) 


Back end
-----------------------
- Create Awards model object + view model (SL)
- Create list of possible awards (descriptions in res/strings) (SL)
- Step counter functionality (ZH) 
- GPS functionality (SL)
- Long-term storage: JS file (ZH) 


- Database: daily trends
	- food intake
	- steps 
	- onDestroy()? update data in DB for current day 
- Hook up database with awards 
- Awards View Model: checks for if user got new awards

Implementation order 
by 10/6 tuesday 
--------------------
- UI scaffolding (basic navigation between pages)
	- UI fragments 
- Awards model object: view model, list, description 
	- Fragment description
- Step counter functionality 

by 10/12 (M) 
--------------------
- long-term storage
- DB 
	- create table + DAO 
	- wire up to awards view model 
- GPS functionality
- sounds! (not a requirement!) 

F 10/16
---------------------
- Tests
- Video 


TODO: 10/11
---------------------
- Awards dates into JSON: ZH 
- Check if gotten award logic in Dao
- Change mochi name in JSON (ZH) 
- Unit tests for critical functions
	- areWeInJapan(): SL
	- createDate(): SL
	- dao tests: SL
	- JSON handler: ZH
	
- Performance tests (Thursday?) 


