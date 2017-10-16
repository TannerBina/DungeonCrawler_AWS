import boto3

#deletes a character from the character table page.
#removes the same character from the userInfo page
def delete_character(event, context):
    #error messages
    ERR_INCORRECT_PARAMS = "__ERR__INCORRECT_PARAMETERS"
    ERR_USERNAME_DNE = "__ERR__INCORRECT_USERNAME"
    
    #success messages
    SUC_CHARACTER_DELETED = "__SUC__CHARACTER_DELETED"
    
    #constant access values
    CHARACTER_TABLE = 'CharacterTable'
    USER_INFO_TABLE = 'UserInfo'
    
    #fetch the character id and username
    try:
        ID = event['id']
        username = event['username']
    except KeyError:
        return {
            'message':ERR_INCORRECT_PARAMS,
            'data':"NULL"
            }
    
    # Get the service resource of dynamodb
    dynamodb = boto3.resource('dynamodb')
    
    #fetch the table for characters
    table = dynamodb.Table(CHARACTER_TABLE)
    
    #delete the item from the char table
    table.delete_item(
        Key={
            'ID':ID
        }    
    )
    
    #get the user info table
    table = dynamodb.Table(USER_INFO_TABLE)
    
    #get the user from the table
    user = table.get_item(
        Key={
            'Username':username
        }
    )
    
    #get the actual item
    try:
        item = user['Item']
    except KeyError:
        return {
            'message':ERR_USERNAME_DNE,
            'data':"NULL"
            }
        
    #fetch teh old character list
    oldChars = item['Characters']
    
    #create the charString to be removed from oldChars
    charString = "__CHAR_ID__"+ID

    #replace the charString with empty
    oldChars = oldChars.replace(charString, "")    
    
    if (len(oldChars) == 0):
        oldChars = "__NO_CHARS__"
    
    #update the character info of the user    
    table.update_item(
        Key={
            'Username':username
        },
        
        UpdateExpression='SET Characters = :val1',
        ExpressionAttributeValues={
            ':val1': oldChars
        }
    )
    
    return {
            'message':SUC_CHARACTER_DELETED,
            'data':"NULL"
            }
    
    