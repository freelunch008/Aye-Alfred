const intent = 'startConversation';
const prompts = require('./prompts');
function buildUserResponse(lexResponse){
return prompts['startConversation'];
}

module.exports={
    buildUserResponse,
    intent
}