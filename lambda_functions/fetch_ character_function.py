import boto3

#fetches a character with the inputted id
#returns character in a long string with definitions __TAG__ATTRIBUTE
def fetch_character(event, context):
    #error messages
    ERR_INCORRECT_PARAMS = "__ERR__INCORRECT_PARAMETERS"
    ERR_CHARACTER_DNE = "__ERR__CHARACTER_DOES_NOT_EXIST"
    
    #success messages
    SUC_CHARACTER_FETCHED = "__SUC__CHARACTER_FETCHED"
    
    #constant access values
    CHARACTER_TABLE = 'CharacterTable'
    
    #attempt to get the character id
    try:
        ID = event['id']
    except KeyError:
        return {
            'message':ERR_INCORRECT_PARAMS,
            'data':"NULL"
            }
        
    # Get the service resource of dynamodb
    dynamodb = boto3.resource('dynamodb')
    
    #fetch the table for characters
    table = dynamodb.Table(CHARACTER_TABLE)
    
    #fetch the character
    character = table.get_item(
        Key={
            'ID':ID
        }
    )
    
    #fetch info contained in item
    try:
        item = character['Item']
    except KeyError:
        return {
            'message':ERR_CHARACTER_DNE,
            'data':"NULL"
            }
    
    #build charString element at a time
    charString = "__ID__%s" %ID
    charString += "__NAME__%s" % item['Name']
    charString += '__CLASS__%s'%item['Class']
    charString += '__RACE__%s' %item['Race']
    charString += '__BASE_HP__%s'%item['BaseHP']
    charString += '__BACKGROUND__%s'%item['Background']
    charString += '__LEVEL__%s'%item['Level']
    charString += '__ALIGNMENT__%s'%item['Alignment']
    charString += item['BaseStats']
    charString += item['ProfList']
    charString += item['FeatList']
    charString += item['SpellList']
    charString += item['ArmorList']
    charString += item['WeaponList']
    charString += item['ItemList']
    
    return {
        'message':SUC_CHARACTER_FETCHED,
        'data':charString
        }