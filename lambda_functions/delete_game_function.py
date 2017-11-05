import boto3

#deletes a game from the game table
def delete_game(event, context):
    #error messages
    ERR_INCORRECT_PARAMS = "__ERR__INCORRECT_PARAMETERS"
    #success message
    SUC_GAME_DELETED = "__SUC__GAME_DELETED"
    
    GAME_TABLE = 'GameTable'
    
    #fetch the gamename to delete
    try:
        name = event['name']
    except KeyError:
        return {
            'message':ERR_INCORRECT_PARAMS,
            'data':"NULL"
        }
    
    #Get the service resource of dynamodb
    dynamodb = boto3.resource('dynamodb')
    
    #fetch the game table
    table = dynamodb.Table(GAME_TABLE)
    
    #delete the item from the table
    table.delete_item(
        Key={
            'Name':name
        }    
    )
    
    return {
        'message':SUC_GAME_DELETED,
        'data':"NULL"
    }