#!/usr/bin/env node
/**
 * Module dependencies.
 */
var program = require('commander');
var syncRequest = require('urllib-sync').request;

/**
 * define default values
 */
program.env = {
	elastic:"http://192.168.99.100:9200/measurement/measurement",
	templateData:{
		"id":null,
		"timestamp":"",
		"equipmentId":1,
		"latitude":"-41.12234234",
		"longitude":"-41.12234234",
		"value":9.2,
		"unit":"m"
	}
}


/**
 * process parans 
 */
program
	.usage('[options] Realiza a carga de dados, gera um numero randomico entre 0 e 40 e envia ao elastic para simulacao de valores')
	.option('-d, --data [type]', 'data base a ser informada, exemplo: 2016-06-17, se não informado realiza carga para o dia de hoje')
	.parse(process.argv);

/**
 * Request functions
 */
function executeRequest(url, args){
	var to = false
	var resp = {};
	try{
		resp = syncRequest(url, args);
	} catch(e) {
		to = true;
	}

	if(to) {
		resp.timeout = true;
	}
	return resp
}

function doPost(url, request){
	var resp = executeRequest(url,{method:'POST', data:request, timeout:100000, headers: {'Content-Type': 'application/json'}});

	if(!resp.timeout) {
		if(resp.status == 200 ||  resp.status == 201){
			console.log("Enviado");
		} else {
			console.log("Erro -> " + resp.status);	
		}
	} else {
		console.log("Time Out");	
	}
}

function padLeft(nr, n, str){
    return Array(n-String(nr).length+1).join(str||'0')+nr;
}


var dataBase;

if(program.data){
	dataBase = program.data;
} else {
	var now = new Date();
	if(now.getMonth() > 9){
		dataBase = now.getFullYear() + "-" + (now.getMonth()+1) + "-" + now.getDate();
	} else {
		dataBase = now.getFullYear() + "-0" + (now.getMonth()+1) + "-" + now.getDate();
	}
}



for(var h = 0; h < 24; h++){
	for(var m = 0; m < 60; m++){
		for(var s = 0; s < 60; s+=5){
			var timestamp = dataBase + "T" + padLeft(h,2) + ":" + padLeft(m,2) + ":" + padLeft(s,2)+".000Z"; 
			var value = Math.floor((Math.random() * 40) + 1);
			
			program.env.templateData.value=value;
			program.env.templateData.timestamp=timestamp;
			doPost(program.env.elastic, program.env.templateData)

			console.log("Enviando informação: value->" + value + ", timestamp->" + timestamp);
		}
	}
}






