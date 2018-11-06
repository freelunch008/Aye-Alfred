const startConversationHandler = require('./intent_startConversation');
const conferenceSetupHandler = require('./intent_conferenceSetup');

function buildBotResponse(messageData){
    let lexResponse=messageData['lexResponse'];
    console.log(lexResponse);
    let intentName=lexResponse.intentName;
    switch (intentName){
        case startConversationHandler.intent:
            return startConversationHandler.buildUserResponse(messageData);
        case conferenceSetupHandler.intent:
            return conferenceSetupHandler.buildUserResponse(messageData);
        default:
            return 'cant handle';
    }
}

module.exports={
    buildBotResponse,
}