var aws = require('aws-sdk');
let lexruntime = new aws.LexRuntime();
const initHandler = require('./init_handler');
exports.handler = (event, context, callback) => {
    let messageBody = event;
    // console.log('messageBody',messageBody);
    // TODO verify hangouts token
    let message = messageBody.message;
    let organizerEmail = message.sender.email;
    let organizerName = message.sender.displayName;
    organizerName=organizerName.replace(' ','_');
    let organizerMessage = message.text;
    console.log('organizerName',organizerName)
    console.log('organizerEmail',organizerEmail);
    console.log('organizerMessage',organizerMessage);

    let botResponse;
    // let params ={
    //     botName:'setupMeet',
    //     botAlias:'Dev',
    //     userId:organizerName.replace(),
    //     contentType:'application/json',
    //     accept:'application/json',
    //     inputStream:organizerMessage
    // };
    // lexruntime.postContent(params, function (err, data) {
    //     if (err) console.log(err, err.stack); // an error occurred
    //     else {
    //          botResponse = initHandler.buildBotResponse(data);
    //          console.log('botResponse',botResponse);
    //          callback(null,{'text':botResponse})
    //     }
    // })
    let params ={
        botName:'setupMeet',
        botAlias:'Dev',
        userId:organizerName,
        inputText:organizerMessage,
        sessionAttributes:{"msg":"hey from user"}
    }
    lexruntime.postText(params, function (err, data) {
        if (err) console.log(err, err.stack); // an error occurred
        else {
             botResponse = initHandler.buildBotResponse({'lexResponse':data,'organizerEmail':organizerEmail});
             console.log('botResponse',botResponse);
             callback(null,{'text':botResponse})
        }
    })
};

