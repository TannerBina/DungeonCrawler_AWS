import boto3

#fetches all games in the game table
#and returns them in format __GAME__gameName_username_password__GAME__etc
def fetch_all_games(event, context):
    #define success message
    SUC_GAMES_FETCHED = "__SUC__GAMES__FETCHED"
    
    #define the game table
    GAME_TABLE = "GameTable"
    
    # Get the service resource of dynamodb
    dynamodb = boto3.resource('dynamodb')
    
    #fetch the table for games
    table = dynamodb.Table(GAME_TABLE)
    
    gameString = "__NO_GAMES__"
    
    allGames = table.scan()
    
    for game in allGames['Items']:
        if gameString == "__NO_GAMES__":
            gameString = ""
        gameString +="__GAME__"
        gameString += game['Name']
        gameString += "_"
        gameString += game['Host']
        gameString += "_"
        gameString += game['Password']
        
    return {
        'message':SUC_GAMES_FETCHED,
        'data':gameString
        }