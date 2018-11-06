import json
import boto3
import uuid

def lambda_handler(event, context):
    # TODO implement
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key'] 
        print('TextFile: ' + key)
        fileUri = "https://s3.amazonaws.com/ankit-speech-to-text/" + key
        # mediaFileUri = event["mediaFileUri"]
        client = boto3.client('transcribe');
        jobName = uuid.uuid4().hex
        response = client.start_transcription_job(
            TranscriptionJobName=jobName,
            LanguageCode='en-US',
            MediaFormat='wav',
            Media={
                'MediaFileUri': fileUri
            },
            OutputBucketName='ankit-speech-to-text'
        )
    return jobName;
