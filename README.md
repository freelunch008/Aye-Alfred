# Aye-Alfred
AI powered assistant that makes scheduling meetings easier

Concept Overview::
Aye ‘Alfred is an AI powered assistant that makes scheduling meetings easier.
Aye ‘Alfred can be fully integrated into your corporate calendar tool of choice.
Simply instruct him to schedule a meeting or a call with a colleague or a team via email, voice or over chat.
Each member of the corporate has got his/her own “Aye ‘Alfred” butler bot who has access to their calendar and knows his/her preferences. 
Aye ‘Alfred automatically schedules for a meeting without anyone’s manual intervention.
It does this by negotiating with the “Aye ‘Alfred” butler bots of the invitees.
Since each of the “Aye ‘Alfred” butler bot knows their master’s preferences and availability, negotiating is automated and a meeting is scheduled which is mutually convenient and at an appropriate time.
That’s it.

![alt tag](https://raw.githubusercontent.com/freelunch008/Aye-Alfred/master/architecture_v1.png)

Architecture Overview::
We have come up with a solution that will facilitate end-to-end process automation of meeting scheduling.
Currently ‘Aye’ Alfred’ application is deployed in AWS and is fully scalable and can also easily be extended to provide more functionality.
Request for scheduling a meeting is supported for multiple channels i.e., through emails, or voice inputs or through an instant messenger. Processing of these requests is handled differently for each of the channels.
This diagram describes the high level structure of ‘Aye’ Alfred’ application.
<<<<<Diagram>>>>
Email based Request:
We have used Amazon Comprehend to process and analyse the email request. Information such as invitees list, purpose of the meeting, when and the location of the meeting are derived by AWS Comprehend and passed on to the Master Scheduler. This Master Scheduler does much of the heavy lifting of understanding the meeting request and arriving at an appropriate meeting time and makes an external Google API call to block calendars of each of the invitees.
For Voice based Request (Input source can be an IVR call):
We have used Amazon Transcribe to translate the voice request to text. And this request is passed on to the Amazon Comprehend service to proceed with the rest of the process.
Chat based Request:
We have used Amazon Lex to interactive arrive at the intent for meeting and capture all the parameters required for the Master Scheduler to proceed with the back end logic of blocking the invitees calendars.


