import json
import boto3

def lambda_handler(event, context):
    # TODO implement
    s3_client = boto3.client('s3')
    s3_resource = boto3.resource('s3')
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key'] 
        print('TextFile: ' + key)
        content_object = s3_resource.Object('ankit-speech-to-text', key)
        file_content = content_object.get()['Body'].read().decode('utf-8')
        json_content = json.loads(file_content)
        print(json_content['results']['transcripts'][0]['transcript'])
