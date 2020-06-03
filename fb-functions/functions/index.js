//const functions = require('firebase-functions');
//const rp = require('request-promise');
//const $ = require('cheerio');
//const admin = require('firebase-admin');
//admin.initializeApp();
//
//
//const url = 'https://www.mohfw.gov.in/';
//
//exports.scheduledStats = functions.pubsub.schedule('1 */1 * * *')
//  .timeZone("Asia/Calcutta")
//  .onRun((context) => {
//		rp(url)
//		  .then((html) => {
//		    //success!
//		    const statNums = [];
//		    for (let i=0; i<$('li > strong', html).length; i++) {
//		    	statNums.push($('li > strong', html)[i].children[0].data);
//		    }
//		    // console.log(statNums);
//		    return admin.app().database('https://washkaro-stats.firebaseio.com/').ref().set({
//		    	'Active': statNums[0],
//		    	'Discharged': statNums[1],
//		    	'Deaths': statNums[2],
//		    	'Migrated': statNums[3]
//		    });
//		    // return null;
//		    // console.log(statNums);
//		  })
//		  .catch((err) => {
//		  	console.log('Some error occured: ', err);
//		  });
//  	return null;
//});