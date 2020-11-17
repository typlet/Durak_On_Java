package Anton.Davidsson.ed;

import java.util.ArrayList;
import java.util.Collections;

public class Cards{
    private ArrayList<Card> packOfCards;
    private ArrayList<Card> floatPack;
    
    public Cards()
    {
        this.packOfCards = new ArrayList<>();
        this.floatPack = new ArrayList<>();
        createPack();
        Collections.shuffle(packOfCards);
    }
    
    private void createPack()
    {
        char[] marks = {'♥','♠', '♣', '♦'};
        String[] names = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for(int i = 0; i < marks.length; i++){
            for(int j = 0; j < names.length; j++){
                packOfCards.add(new Card(names[j], j, marks[i]));
            }
        }
    }
    
    public void putCardOnFloat(Card card)
    {boolean existOnFloat=false;
        for(Card c:floatPack){
            if(card!=null&&c==card){
                existOnFloat=true;
            }
        }
        if(!existOnFloat){
        
            floatPack.add(card);
        }else {
            System.out.println("Already exist's");
        }
        
    }
    
    public void showFloat()
    {
        System.out.println("\n");
        for(Card card : this.floatPack){
            new waitForMeCard().run();
            System.out.print("{" + card.getName() + card.getMark() + "}");
        }
        System.out.println("\n");
    }
    
    public ArrayList<Card> getPackOfCards()
    {
        return packOfCards;
    }
    
    public ArrayList<Card> getFloatPack()
    {
        return floatPack;
    }
    
    public void clearTable()
    {
        floatPack.clear();
    }
}
class waitForMeCard implements Runnable{
    @Override
    public void run()
    { try{
        Thread.sleep(500);
    }catch(InterruptedException e){
        e.getStackTrace();
    }
    
    }
}
