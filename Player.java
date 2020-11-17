package Anton.Davidsson.ed;

import java.util.ArrayList;

public class Player{
    private String name;
    private ArrayList<Card> playerHand;
    private boolean isBot;
    
    public Player(String name, boolean isBot)
    {
        this.name = name;
        this.playerHand = new ArrayList<>();
        this.isBot = isBot;
    }
    
    public void setPlayerHand(ArrayList<Card> cards)
    {
        ArrayList<Card> tempCards = new ArrayList<>(cards);
        
        for(Card card : tempCards){
            if(playerHand.size() >= 6){
                break;
            }
            playerHand.add(card);
            cards.remove(card);
        }
        
    }
    
    public void removeCardFromHand(Card card)
    {
        if(card != null){
            if(playerHand.size() > 0)
                playerHand.remove(card);
        }
    }
    
    public void addCardToHand(Card card)
    {
        boolean existOnFloat=false;
        for(Card c:playerHand){
            if(card!=null&&c==card){
                existOnFloat=true;
            }
        }
        if(!existOnFloat){
        
            playerHand.add(card);
        }else {
            System.out.println("card -"+card.getName()+card.getMark());
            System.out.println("Already exist's");
        }
    }
    
    public ArrayList<Card> getPlayerHand()
    {
        return playerHand;
    }
    
    public boolean isBot()
    {
        return isBot;
    }
    
    public String getName()
    {
        return name;
    }
}
