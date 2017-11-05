#import library for accessing dynamodb
import boto3

#checks to see if the username is used in the given table
def check_used(table, name):
    #check if there already exists an item with same username
    check = table.get_item(
        Key={
            'Name':name
        }
    )
    
    #attempt to see if there is an item contained
    try:
        item = check['Item']
        return True
    except KeyError:
        return False
        
def create_game(event, context):
    #define error messages
    ERR_GAME_NAME_EXISTS = "__ERR__GAME_NAME_EXISTS"
    ERR_GAME_NAME_TOO_LONG = "__ERR__GAME_NAME_TOO_LONG"
    ERR_GAME_NAME_TOO_SHORT = "__ERR__GAME_NAME_TOO_SHORT"
    ERR_PASSWORD_SHORT = "__ERR__PASSWORD_TOO_SHORT"
    ERR_PASSWORD_LONG = "__ERR__PASSWORD_TOO_LONG"
    ERR_INCORRECT_PARAMS = "__ERR__INCORRECT_PARAMETERS"
    
    #define constant success message
    SUC_CREATE_SUCCESS = "__SUC__CREATE_SUCCESS"
    
    #define constant access values
    GAME_TABLE = "GameTable"
    LENGTH_MIN = 5
    LENGTH_MAX = 26
    
    try:
        name = event['name']
        password = event['password']
        username = event['username']
    except KeyError:
        return {
            'message':ERR_INCORRECT_PARAMS,
            'data':"NULL"
            }
    
    # check to make sure length of username and password
    # is valid, if not return error
    if (len(name) < LENGTH_MIN):
        return {
            'message':ERR_USERNAME_SHORT,
            'data':"NULL"
            }
    if (len(password) < LENGTH_MIN):
        return {
            'message':ERR_PASSWORD_SHORT,
            'data':"NULL"
            }
    if (len(name) > LENGTH_MAX):
        return {
            'message':ERR_USERNAME_LONG,
            'data':"NULL"
            }
    if (len(password) > LENGTH_MAX):
        return {
            'message':ERR_PASSWORD_LONG,
            'data':"NULL"
            }
            
            
    # Get the service resource of dynamodb
    dynamodb = boto3.resource('dynamodb')
    
    #fetch the table for user info
    table = dynamodb.Table(GAME_TABLE)

    #if the username already exists return error
    #checked by determining if corresponding http packet
    #contains information or not (length 2)
    if (check_used(table, name)):
        return {
            'message':ERR_GAME_NAME_EXISTS,
            'data':"NULL"
            }
            
    #put the info into the database
    table.put_item(
        Item={
            'Name':name,
            'Password':password,
            'Host':username
        }
    )
    
    #return succesfully created flag
    return {
            'message':SUC_CREATE_SUCCESS,
            'data':"NULL"
            }
    