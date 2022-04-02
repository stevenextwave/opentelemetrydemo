'use strict';

const uuid = require('uuid');
const AWS = require('aws-sdk');


const sqs = new AWS.SQS();
const sqsUrl = process.env.sqs_url;

module.exports.submit = (event, context, callback) => {
    console.log("Receieved request submit candidate details. Event is", event);
    const requestBody = JSON.parse(event.body);
    const fullname = requestBody.fullname;
    const email = requestBody.email;
    const experience = requestBody.experience;
    const skills = requestBody.skills;
    const recruiterEmail = requestBody.recruiterEmail;

    if (typeof fullname !== 'string' || typeof email !== 'string' || typeof experience !== 'number' || typeof skills !== 'string' || typeof recruiterEmail !== 'string') {
        console.error('Validation Failed');
        callback(new Error('Couldn\'t submit candidate because of validation errors.'));
        return;
    }
    const candidate = candidateInfo(fullname, email, experience, skills, recruiterEmail);
    const params = {
        MessageBody: JSON.stringify(candidate), 
        QueueUrl: sqsUrl
    };
    sqs.sendMessage(params, function(err, data) {
        if (err) {
            console.error('Failed to submit candidate to system', err);
            callback(null, failureResponseBuilder(
                409,
                JSON.stringify({
                    message: `Unable to submit candidate with email ${email}`
                })
            ))
        }
        else  {
            console.log(`Successfully submitted ${fullname}(${email}) candidate to system`);
            callback(null, successResponseBuilder(
                JSON.stringify({
                    message: `Sucessfully submitted candidate with email ${email}`,
                    candidateId: res.id
                }))
            );
        }
    });
};


const successResponseBuilder = (body) => {
    return {
        statusCode: 200,
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        body: body
    };
};

const failureResponseBuilder = (statusCode, body) => {
    return {
        statusCode: statusCode,
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        body: body
    };
};


const candidateInfo = (fullname, email, experience, skills, recruiterEmail) => {
    const timestamp = new Date().getTime();
    return {
        id: uuid.v1(),
        fullname: fullname,
        email: email,
        experience: experience,
        skills: skills,
        recruiterEmail: recruiterEmail,
        evaluated: false,
        submittedAt: timestamp,
        updatedAt: timestamp,
    };
};
