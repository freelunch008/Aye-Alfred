const intent='conferenceSetup';
const prompts = require('./prompts');
const utcTimeConstant = require('./utcTimeConstant').utcTimeConstants;

function buildUserResponse(messageData){
    console.log('lexResponse  ',messageData);
    let lexResponse=messageData['lexResponse'];
    let lexSlots = lexResponse['slots'];
    if(!lexSlots['colleagueOne']){
        return prompts['getColleagueOneName'];
    }
    if(!lexSlots['day']){
        return prompts['getDay'];
    }
    if(!lexSlots['meetLocation'])
        return prompts['getLocation'];
    if(!lexSlots['eventTitle'])
        return prompts['eventTitle'];
    setCalenderEventData(messageData);
    return lexResponse['message'];
}

function setCalenderEventData(messageData){
    let lexResponse=messageData['lexResponse'];
    let lexSlots = lexResponse['slots'];
    
    let day = lexSlots['day'];
    let time = lexSlots['time'];
    let relativeTime = lexSlots['relativeTime'];
    let startTimeDateObj;
    let endTimeDateObj;
    let timeObj={};

    
    if(time&&(time=='EV'||time=='MO')){
        if(time=='EV'){
            if(relativeTime&&relativeTime=='before'){
                timeObj=buildTimeObjs('evening_before',day);
            }
            else{
                timeObj=buildTimeObjs('evening_after',day);
            }
        }
        if(time=='MO'){
            if(relativeTime&&relativeTime=='before'){
                timeObj=buildTimeObjs('morning_before',day);
            }
            else{
                timeObj=buildTimeObjs('morning_after',day);
            }
        }
    }
    else if(time&&(time!='AF'||time!='NI')){
        timeObj = buildTimeObjFromClock(day,time,relativeTime);
    }else{
        startTimeDateObj = new Date(day + "T00:00:00Z");
        timeObj['startTimeInTimeStamp'] = startTimeDateObj.getTime();
        endTimeDateObj = new Date(day+"T09:00:00Z");
        timeObj['endTimeInTimeStamp'] = endTimeDateObj.getTime();
    }
    let colleagueOne = lexSlots['colleagueOne'];
    let attendees = [{'name':colleagueOne}];
    if(lexSlots['colleagueTwo'])
        attendees.push({'name':lexSlots['colleagueTwo']});
    
    let calenderEventData={
       "organizerEmail":messageData['organizerEmail'],
       "startDate":timeObj['startTimeInTimeStamp'],
       "endDate":timeObj['endTimeInTimeStamp'],
       "duration":30,
       "attendees":attendees,
       "location":lexSlots['meetLocation'],
       "description":"",
       "summary":lexSlots["eventTitle"],
    }
    console.log('calenderEventData',calenderEventData)
}


function buildTimeObjs(utcTimeKey,day) {
    console.log('utcTimeConstant',utcTimeConstant);
    let utcTimeConstantArr = utcTimeConstant[utcTimeKey];
    let timeObj={};
    let startTimeDateObj = new Date(day + utcTimeConstantArr[0]);
    let endTimeDateObj = new Date(day+utcTimeConstantArr[1]);
    timeObj['startTimeInTimeStamp'] = startTimeDateObj.getTime();
    timeObj['endTimeInTimeStamp']=endTimeDateObj.getTime();
    return timeObj;
}
function buildTimeObjFromClock(day,time,relativeTime)
{
    let hours = time.split(":");
    let startHour;
    let endHour;
    if(relativeTime&&relativeTime=='before'){
        startHour=parseInt(hours[0], 10)-6;
        endHour=hours[0]
    }
    else{
        startHour=hours[0];
        endHour=parseInt(hours[0], 10)+6;
    }
    console.log('buildTimeObjFromClock  '+startHour+'  '+endHour);
    let timeObj={};
    let utcMinSec="00:00Z"
    let utcStartTime = "T"+startHour+":"+utcMinSec;
    let utcEndTime = "T"+endHour+":"+utcMinSec;
    let startTimeDateObj = new Date(day + utcStartTime);
    let endTimeDateObj = new Date(day+utcEndTime);
    timeObj['startTimeInTimeStamp'] = startTimeDateObj.getTime();
    timeObj['endTimeInTimeStamp']=endTimeDateObj.getTime();
    return timeObj;
}
module.exports={
    buildUserResponse,
    intent
}