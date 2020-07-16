const mysql = require('mysql2');
const express = require('express');
const httpPort=3001;
const app = express();

const pool = mysql.createPool({
	host: 'localhost',
	user: 'iot',
	password: 'iot',
	database: 'iot',
	connectionLimit: 10,
	queueLimit: 0,
	waitForConnections: true,
	timezone: 'Z'

});

app.get('/addAlert/:type/:value/:seuil', function(req,res){
	var request = "INSERT INTO `historique` (`id`, `val`, `seuil`, `nomCapteur`, `heure`) VALUES (NULL, ?, ?, ?, CURRENT_TIMESTAMP) ";

	pool.query(request,
		[req.params.value,req.params.seuil,req.params.type],
		function(err,results){
			if(err)
				res.json(err);
			else
				res.json(results);


		})
})

app.get('/getHistory',function(req,res){
	var request = "SELECT * FROM `historique`";
	pool.query(request,function(err,results){

		if(err)
			res.json(err)
		else
			res.json(results);

	})

})

app.listen(httpPort);