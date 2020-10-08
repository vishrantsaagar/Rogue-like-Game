import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DungeonXMLHandler extends DefaultHandler {
    
    private StringBuilder data = null;

    private Stack<Displayable> dispstack = null;
    private Stack<Action> actstack = null;

    private Dungeon dungeon = null;
    private Room currRoom = null;
    private Creature currCreature = null;
    private Action currAction = null;
    private Item currItem = null;
    private Armor currArmor = null;
    private Passage currPassage = null;
    private Sword currSword = null;

    public Dungeon getDungeon(){
        return dungeon;
    }

    private boolean bvisible = false;
    private boolean bposX = false;
    private boolean bposY = false;
    private boolean bwidth = false;
    private boolean bheight = false;
    private boolean bhp = false;
    private boolean bmaxhit = false;
    private boolean bactionMessage = false;
    private boolean bactionIntValue = false;
    private boolean bactionCharValue = false;
    private boolean bItemIntValue = false;
    private boolean btype = false;
    private boolean bhpMoves = false;

    public DungeonXMLHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    
        if (qName.equalsIgnoreCase("Dungeon")) {
            String dunName = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            
            ObjectDisplayGrid.getObjectDisplayGrid(gameHeight, width, topHeight, bottomHeight);
            
            dungeon.getDungeon(dunName, gameHeight, width);

            //dispstack.push(dungeon); do we have to make dungeon an extended version of Displayable or do we not need to push it on there?

        } else if(qName.equalsIgnoreCase("Rooms")) { //order followed in testDrawing.xml

        } else if(qName.equalsIgnoreCase("Room")) {
            int roomid = Integer.parseInt(attributes.getValue("room"));
            Room room = new Room(roomid);
            dungeon.addRoom(room);
            currRoom = room; 

            dispstack.push(currRoom);

        } else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } else if(qName.equalsIgnoreCase("width")) {
            bwidth = true;
        } else if(qName.equalsIgnoreCase("height")) {
            bheight = true;
        } else if(qName.equalsIgnoreCase("Monster")) {

            String monName = attributes.getValue("name");
            int monRoom = Integer.parseInt(attributes.getValue("room"));
            int monSerial = Integer.parseInt(attributes.getValue("serial"));
            
            Monster m1 = new Monster();
            m1.setName(monName);
            m1.setID(monRoom, monSerial);

            dungeon.addCreature(m1);
            currCreature = m1;

            dispstack.push(currCreature);

        } else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } else if(qName.equalsIgnoreCase("type")) {
            btype = true;
        } else if(qName.equalsIgnoreCase("hp")) {
            bhp = true;
        } else if(qName.equalsIgnoreCase("maxhit")) {
            bmaxhit = true;

        } else if(qName.equalsIgnoreCase("CreatureAction")) {

            String creatureName = attributes.getValue("name");
            String creatureType = attributes.getValue("type");
            CreatureAction c1 = null;

            if(creatureName == "Remove"){
                c1 = new Remove(creatureName, currCreature); 
            }

            else if(creatureName == "YouWin"){
                c1 = new YouWin(creatureName, currCreature); 
            }

            else if(creatureName == "ChangeDisplayType"){
                c1 = new ChangedDisplayType(creatureName, currCreature);
            }

            else if(creatureName == "UpdateDisplay"){
                c1 = new UpdateDisplay(creatureName, currCreature);
            }

            else if(creatureName == "EndGame"){
                c1 = new EndGame(creatureName, currCreature);
            }

            if(creatureType == "death"){
                currCreature.setDeathAction(c1);
            }

            else if(creatureType == "hit"){
                currCreature.setHitAction(c1);
            }

            currAction = c1;
            actstack.push(currAction);
        } 
        else if(qName.equalsIgnoreCase("actionMessage")) {
            bactionMessage = true;

        } else if(qName.equalsIgnoreCase("actionIntValue")) {
            bactionIntValue = true;
        
        } else if(qName.equalsIgnoreCase("actionCharValue")) {
            bactionCharValue = true;
        
        }else if(qName.equalsIgnoreCase("Scroll")) {

            String scrollName = attributes.getValue("name");
            int scrollRoom = Integer.parseInt(attributes.getValue("room"));
            int scrollSerial = Integer.parseInt(attributes.getValue("serial"));

            Scroll s1 = new Scroll(scrollName);
            s1.setID(scrollRoom, scrollSerial);
            currItem = s1;

            dispstack.push(currItem);

        } else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;

        }else if(qName.equalsIgnoreCase("ItemAction")) {

            String itemName = attributes.getValue("name");
            //String itemType = attributes.getValue("type"); unnecessary?

            if(itemName == "BlessArmor"){
                BlessCurseOwner bco1 = new BlessCurseOwner(currItem); //what parameters do we pass through remove - what is name and owner
                currItem.addItemAction(bco1);
                currAction = bco1; 
            }

            else if(itemName == "Hallucinate"){
                Hallucinate h1 = new Hallucinate(currItem);
                currItem.addItemAction(h1);
                currAction = h1;
            }

            actstack.push(currAction);

        }else if(qName.equalsIgnoreCase("actionMessage")) {
                bactionMessage = true;
    
        } else if(qName.equalsIgnoreCase("actionIntValue")) {
                bactionIntValue = true;
            
        } else if(qName.equalsIgnoreCase("actionCharValue")) {
                bactionCharValue = true;
            
        } else if(qName.equalsIgnoreCase("Player")) {
            //String player_name = attributes.getValue("name");
            //int playerRoom = Integer.parseInt(attributes.getValue("room"));
            //int playerSerial = Integer.parseInt(attributes.getValue("serial"));

            //Do we need to push this dispstack?

        } else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } else if(qName.equalsIgnoreCase("hp")) {
            bhp = true;
        } else if(qName.equalsIgnoreCase("hpMoves")) {
            bhpMoves = true;
        } else if(qName.equalsIgnoreCase("maxhit")) {
            bmaxhit = true;

        }else if(qName.equalsIgnoreCase("Armor")){
            String armor_name = attributes.getValue("name");
            int armor_room = Integer.parseInt(attributes.getValue("room"));
            int armor_serial = Integer.parseInt(attributes.getValue("serial"));

            Armor a1 = new Armor(armor_name);
            a1.setName(armor_name);
            a1.setID(armor_room, armor_serial);
            
            currArmor = a1;
            dispstack.push(currArmor);
        }

        else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        }

        else if(qName.equalsIgnoreCase("ItemIntValue")){
            bItemIntValue = true;
        }

        else if(qName.equalsIgnoreCase("Sword")){
            String swordName = attributes.getValue("name");
            int swordRoom = Integer.parseInt(attributes.getValue("room"));
            int swordSerial = Integer.parseInt(attributes.getValue("serial"));

            Sword sw1 = new Sword(swordName);
            sw1.setID(swordRoom, swordSerial);
            
            currSword = sw1;
            dispstack.push(currSword);
        }

        else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } else if(qName.equalsIgnoreCase("ItemIntValue")){
            bItemIntValue = true;
        }else if(qName.equalsIgnoreCase("Passages")){ 

        }else if(qName.equalsIgnoreCase("Passage")){

            int room1 = Integer.parseInt(attributes.getValue("room1"));
            int room2 = Integer.parseInt(attributes.getValue("room2"));     
            Passage p1 = new Passage();
            p1.setID(room1, room2);
            currPassage = p1;

            dispstack.push(currPassage);
        }       
        else if(qName.equalsIgnoreCase("visible")) {
            bvisible = true;
        } else if(qName.equalsIgnoreCase("posX")) {
            bposX = true;
        } else if(qName.equalsIgnoreCase("posY")) {
            bposY = true;
        } else if(qName.equalsIgnoreCase("width")) {
            bwidth = true;
        } else if(qName.equalsIgnoreCase("height")) {
            bheight = true;
        }       
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Room room;
        Monster monster;
        CreatureAction creature_action;

        if(bvisible){
            
        }

        else if(bposX){

        }

        else if(bposY){

        }

        else if(bwidth){

        }

        else if(bheight){

        }

        else if(bhp){

        }

        else if(bmaxhit){

        }

        else if(bactionMessage){

        }

        else if(bactionCharValue){

        }

        else if(bactionIntValue){

        }

        else if(bItemIntValue){

        }

        else if(btype){

        }

        else if(bhpMoves){

        }
    }


    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    //@Override
    //public String toString(){
    //    String str = "DungeonXMLHandler\n";

    //    for(int i = 0; i < rooms.size(); i++){
    //        str += rooms.get(i).toString() + "\n";
    //    }

    //    str += "roomsBeingParsed: " + roomsParsed.toString() + "\n";
    //    //str += "creaturesBeingParsed: " + creaturesParsed + "\n";
    //    str += "bvisible: " + bvisible + "\n";
    //    str += "bposX: " + bposX + "\n";
    //    str += "bposY: " + bposY + "\n";
    //    str += "bwidth: " + bwidth + "\n";
    //    str += "bheight: " + bheight + "\n";

    //    //Need to still add rooms, room, monster, player, and scroll
    //    return str;
    //}

}