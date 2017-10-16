#import library for accessing dynamodb
import boto3

#checks to see if the username is used in the given table
def check_used(table, username):
    #check if there already exists an item with same username
    check = table.get_item(
        Key={
            'Username':username
        }
    )
    
    #attempt to see if there is an item contained
    try:
        item = check['Item']
        return True
    except KeyError:
        return False

#define function to create user with username, password_1 and 2
def create_user(event, context):
    #define constant error messages
    ERR_USERNAME_EXISTS = "__ERR__USERNAME_EXISTS"
    ERR_PASSWORD_MATCH = "__ERR__PASSWORD_MATCH"
    ERR_USERNAME_SHORT = "__ERR__USERNAME_TOO_SHORT"
    ERR_PASSWORD_SHORT = "__ERR__PASSWORD_TOO_SHORT"
    ERR_USERNAME_LONG = "__ERR__USERNAME_TOO_LONG"
    ERR_PASSWORD_LONG = "__ERR__PASSWORD_TOO_LONG"
    ERR_INCORRECT_PARAMS = "__ERR__INCORRECT_PARAMETERS"
    
    #define constant success message
    SUC_CREATE_SUCCESS = "__SUC__CREATE_SUCCESS"
    
    #define constant access values
    USER_INFO_TABLE = "UserInfo"
    LENGTH_MIN = 5
    LENGTH_MAX = 26
    
    #define no character value
    NO_CHARS = "__NO_CHARS__"
    
    #fetch username and two password inputs
    try:
        username = event['username']
        password_1 = event['password_1']
        password_2 = event['password_2']
    except KeyError:
        return {
            'message':ERR_INCORRECT_PARAMS,
            'data':"NULL"
            }
    
    # check to make sure length of username and password
    # is valid, if not return error
    if (len(username) < LENGTH_MIN):
        return {
            'message':ERR_USERNAME_SHORT,
            'data':"NULL"
            }
    if (len(password_1) < LENGTH_MIN):
        return {
            'message':ERR_PASSWORD_SHORT,
            'data':"NULL"
            }
    if (len(username) > LENGTH_MAX):
        return {
            'message':ERR_USERNAME_LONG,
            'data':"NULL"
            }
    if (len(password_1) > LENGTH_MAX):
        return {
            'message':ERR_PASSWORD_LONG,
            'data':"NULL"
            }
    
    #check for matching passwords
    if (password_1 != password_2):
        return {
            'message':ERR_PASSWORD_MATCH,
            'data':"NULL"
            }
    
    # Get the service resource of dynamodb
    dynamodb = boto3.resource('dynamodb')
    
    #fetch the table for user info
    table = dynamodb.Table(USER_INFO_TABLE)

    #if the username already exists return error
    #checked by determining if corresponding http packet
    #contains information or not (length 2)
    if (check_used(table, username)):
        return {
            'message':ERR_USERNAME_EXISTS,
            'data':"NULL"
            }
    
    #put the info into the database
    table.put_item(
        Item={
            'Username':username,
            'Password':password_1,
            'Characters':NO_CHARS
        }
    )
    
    #return succesfully created flag
    return {
            'message':SUC_CREATE_SUCCESS,
            'data':"NULL"
            }