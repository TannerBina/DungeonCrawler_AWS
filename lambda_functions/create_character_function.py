import boto3
import uuid

#checks if ID is used in the given table
def used(ID, table):
    #get the package for the id
    check = table.get_item(
        Key={
            'ID':ID
        }
    )
    
    #see if the package contains info
    try:
        item = check['Item']
        #if it times it is used
        return True
    except KeyError:
        #else it isnt used
        return False
    

#creates a character for the user inputted.
#requires all character fields which are
#name, race, class, background, alignment,
#base stats, base hp, level, featList, spellList,
#weaponList, armorList, itemList
#also requires inputted username for UserInfo update
def create_character(event, context):
    #all error messages
    ERR_INCORRECT_PARAMS = "__ERR__INCORRECT_PARAMETERS"
    
    #all success messages
    SUC_CHAR_CREATED = "__SUC__CHARACTER_CREATED"
    
    #all constant access values
    CHAR_TABLE = 'CharacterTable'
    USER_INFO_TABLE = 'UserInfo'
    NO_CHAR_STR = '__NO_CHARS__'
    
    #attempt to grab all necesary params
    try:
        username = event['username']
        #all character fiews
        name = event['name']
        race = event['race']
        classID = event['class']
        background = event['background']
        alignment = event['alignment']
        #base stats should be __STAT__NUM__STAT__NUM so on
        baseStats = event['baseStats']
        baseHP = event['baseHP']
        level = event['level']
        #featList should be in form __FEAT__NAME__FEAT__NAME so on
        featList = event['featList']
        #spellList should be in form __SPELL__NUM__NAME__SPELL__NUM__NAME
        spellList = event['spellList']
        #form of __WEAPON__NAME_DICE_BONUS_FINESS
        weaponList = event['weaponList']
        #form of __ARMOR__NAME_AC_DEXMAX_SHIELD
        armorList = event['armorList']
        #form of __ITEM__NAME__ITEM__NAME
        itemList = event['itemList']
    except KeyError:
        return ERR_INCORRECT_PARAMS
    
    
    # Get the service resource of dynamodb
    dynamodb = boto3.resource('dynamodb')
    #get the dynamodb cleint
    client = boto3.client('dynamodb')
    
    #fetch the table for user info
    table = dynamodb.Table(CHAR_TABLE)
    
    #generate a randomHexID for the character
    charID = uuid.uuid4().hex
    
    #while that charID is being used, generate a new one
    while(used(charID, table)):
        charID = uuid.uuid4().hex
        
    #put the character into the character table    
    table.put_item(
        Item={
            'ID':charID,
            'Name':name,
            'Class':classID,
            'Background':background,
            'Alignment':alignment,
            'BaseStats':baseStats,
            'BaseHP':baseHP,
            'Level':level,
            'FeatList':featList,
            'SpellList':spellList,
            'WeaponList':weaponList,
            'ArmorList':armorList,
            'ItemList':itemList
        }
    )
    
    #change table to the userInfo table
    table = dynamodb.Table(USER_INFO_TABLE)
    
    #get the user info
    userInfo = table.get_item(
        Key={
            'Username':username
        }    
    )
    
    #get all user characters
    userChars = userInfo['Item']['Characters']
    
    #check if no characters update char string
    if (userChars == NO_CHAR_STR):
        charString = '__CHAR_ID__' + charID
    else :
        charString = userChars + '__CHAR_ID__' + charID
        
    #update the character info of the user    
    table.update_item(
        Key={
            'Username':username
        },
        
        UpdateExpression='SET Characters = :val1',
        ExpressionAttributeValues={
            ':val1': charString
        }
    )
    
    return SUC_CHAR_CREATED