package Anton.Davidsson.ed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class Game{
    
    private ArrayList<Player> players;
    private Cards cards;
    private Card leadMark;
    private int player = 0;
    private boolean checkBita = false;
    private int round = 1;
    
    public Game()
    {
        this.players = new ArrayList<>();
        this.cards = new Cards();
        this.leadMark = gameGetLeaderCard();
        setPlayers(1, false);
        setPlayers(2, true);
        
        startGame();
    }
    
    private void startGame()
    {
        boolean win = false;
        boolean canAddCards;
        ArrayList<Card> defenderTempCards;
        ArrayList<Card> attackerTempCards = new ArrayList<>();
        while(!win){
            System.out.println("( Round - " + round + " )");
            setPlayersHands(players.size());
            
            int nextPlayer = (player + 1);
            if(nextPlayer >= players.size()){
                nextPlayer = 0;
            }
            System.out.println("\n[" + cards.getPackOfCards().size() + "] " + "Cards left :Leader Card :" + "{" + leadMark.getName() + leadMark.getMark() + "}" + "" + "\n [Press 'T' to take/ do nothing");
            
            Card playerCard = playerMove(player, players.get(player).isBot());
            if(playerCard == null){
                System.out.println("Must make a move");
                
                continue;
            }
            cards.putCardOnFloat(playerCard);
            players.get(player).removeCardFromHand(playerCard);
            
            canAddCards = checkTable(players.get(player).getPlayerHand());
            System.out.println("----------------------------------1-");
            cards.showFloat();
            new waitForMe().run();
            System.out.println("-----------------------------------");
            
            while(canAddCards){
                Card cardToAdd = addCards(player, cards.getFloatPack(), players.get(player).isBot());
                new waitForMe().run();
                if(cardToAdd != null){
                    cards.putCardOnFloat(cardToAdd);
                    players.get(player).removeCardFromHand(cardToAdd);
                    canAddCards = checkTable(players.get(player).getPlayerHand());
                    
                }else{
                    
                    canAddCards = false;
                }
            }
            
            defenderTempCards = defendMove(nextPlayer, cards.getFloatPack());
            
            new waitForMe().run();
            
            if(defenderTempCards != null){
                putCardsOnTable(defenderTempCards);
                removeCardsFromPlayer(nextPlayer, defenderTempCards);
                
                System.out.println("-----------------------------3------");
                cards.showFloat();
                new waitForMe().run();
                System.out.println("-----------------------------------");
                canAddCards = checkTable(players.get(player).getPlayerHand());
                
                while(canAddCards){
                    if(checkBita){
                        break;
                    }
                    Card cardToAdd = addCards(player, cards.getFloatPack(), players.get(player).isBot());
                    if(cardToAdd != null){
                        cards.putCardOnFloat(cardToAdd);
                        if(attackerTempCards != null){
                            attackerTempCards.clear();
                            attackerTempCards.add(cardToAdd);
                        }
                        
                        defenderTempCards = defendMove(nextPlayer, attackerTempCards);
                        if(defenderTempCards != null){
                            putCardsOnTable(defenderTempCards);
                            removeCardsFromPlayer(nextPlayer, defenderTempCards);
                        }else{
                            System.out.println("Taking cards");
                            playerTakeCards(nextPlayer, cards.getFloatPack());
                            checkBita = true;
                            
                        }
                        
                        System.out.println("--------------------------5---------");
                        cards.showFloat();
                        System.out.println("-----------------------------------");
                        canAddCards = checkTable(players.get(player).getPlayerHand());
                    }
                    
                }
            }else{
                System.out.println("Taking cards");
                playerTakeCards(nextPlayer, cards.getFloatPack());
                checkBita = true;
                cards.showFloat();
                cards.clearTable();
                
            }
            if(checkBita){
                System.out.println("----------------------------6-------");
                cards.showFloat();
                new waitForMe().run();
                System.out.println("-----------------------------------");
                cards.clearTable();
                checkBita = false;
                System.out.println("\n{{{Player =>" + (nextPlayer + 1) + "}}} Take cards...");
            }else{
                player++;
                System.out.println("--------Bitaaaaa*_&_^_$_#_-------");
                cards.showFloat();
            }
            if(player >= players.size()){
                player = 0;
            }
            cards.clearTable();
            try{
                attackerTempCards.clear();
                defenderTempCards.clear();
            }catch(NullPointerException e){
            
            }
            System.out.println("player " + (player + 1) + " move");
            new waitForMe().run();
            System.out.println("--------------");
            win =
                    
                    checkForWinner(players);
        }
            round++;
    }
    
    private ArrayList<Card> defendMove(int player, ArrayList<Card> cardsToDefend)
    {
        if(players.get(player).isBot()){
            boolean defeated = false;
            int cardToDefSize = cardsToDefend.size();
            Card defeatedCard = null;
            
            ArrayList<Card> botDefCards = new ArrayList<>();
            ArrayList<Card> tempCardsToDef = new ArrayList<>(cardsToDefend);
            ArrayList<Card> tempPlayerDefCards = new ArrayList<>(players.get(player).getPlayerHand());
            
            Iterator<Card> iterator = tempPlayerDefCards.iterator();
            while(iterator.hasNext()){
                Card tempCard = iterator.next();
                iterator.remove();
                if(defeated){
                    tempCardsToDef.remove(defeatedCard);
                    System.out.println("defeated " + defeatedCard.toString());
                    defeated = false;
                }
                
                for(Card card : tempCardsToDef){
                    if(checkIfCardCanDef(card, tempCard)){
                        botDefCards.add(tempCard);
                        players.get(player).removeCardFromHand(tempCard);
                        System.out.println(tempCard.toString() + " defending ");
                        defeatedCard = card;
                        defeated = true;
                        cardToDefSize--;
                        break;
                        
                    }
                    
                }
                if(cardToDefSize == 0){
                    return botDefCards;
                }
            }
            
        }
        if(!players.get(player).isBot()){
            boolean defeat = false;
            Card defeatedCard = null;
            ArrayList<Card> playerDefendCards = new ArrayList<>();
            ArrayList<Card> cardsToDef = new ArrayList<>(cardsToDefend);
            
            while(true){
                if(defeat){
                    cardsToDef.remove(defeatedCard);
                    defeat = false;
                }
                Card playerCard = playerMove(player, players.get(player).isBot());
                if(playerCard != null){
                    if(playerCard.getValue() == -1 && playerCard.getMark() == 'f'){
                        return null;
                    }
                    for(Card card : cardsToDef){
                        if(checkIfCardCanDef(card, playerCard)){
                            defeat = true;
                            defeatedCard = card;
                            playerDefendCards.add(playerCard);
                            players.get(player).removeCardFromHand(playerCard);
                            break;
                        }
                    }
                }else{
                    return null;
                }
                if(playerDefendCards.size() == cardsToDefend.size()){
                    
                    return playerDefendCards;
                }
            }
        }
        return null;
    }
    
    private Card gameGetLeaderCard()
    {
        ArrayList<Card> tempCards = cards.getPackOfCards();
        int random = (int) (Math.random() * tempCards.size());
        Card tempCard = cards.getPackOfCards().get(random);
        cards.getPackOfCards().set(random, cards.getPackOfCards().get(cards.getPackOfCards().size() - 1));
        cards.getPackOfCards().set(cards.getPackOfCards().size() - 1, tempCard);
        
        return tempCard;
    }
    
    private Card playerMove(int player, boolean isBot)
    {
        cards.showFloat();
        Card tempCard = null;
        if(!isBot){
            
            ArrayList<Card> tempHand = players.get(player).getPlayerHand();
            Scanner scanner = new Scanner(System.in);
            for(Card card : tempHand){
                System.out.print("{" + card.getMark() + card.getName() + "}");
                System.out.println("{" + players.get(player).getPlayerHand().indexOf(card) + "}");
            }
            
            System.out.println("please make a move");
            String input = scanner.nextLine();
            try{
                if(Integer.valueOf(input) < tempHand.size()){
                    
                    Card card = players.get(player).getPlayerHand().get(Integer.valueOf(input));
                    System.out.println(card.getName() + card.getMark());
                    tempCard = card;
                    
                    return tempCard;
                }else if(input.equals("T")){
                    return null;
                }else{
                    System.out.println("i don't have this card");
                    playerMove(player, isBot);
                }
                
            }catch(NumberFormatException e){
                return null;
            }
            
        }else{
            int value = Integer.MAX_VALUE;
            ArrayList<Card> botHand = new ArrayList<>(players.get(player).getPlayerHand());
            for(Card card : botHand){
                if(card.getValue() < value){
                    value = card.getValue();
                    tempCard = card;
                }
            }
            if(tempCard != null)
                System.out.println("Attacking with " + tempCard.toString());
            return tempCard;
        }
        return null;
    }
    
    private boolean checkTable(ArrayList<Card> cardsArr)
    {
        for(Card playerCard : cardsArr){
            for(Card tableCard : cards.getFloatPack()){
                if(playerCard.getValue() == tableCard.getValue()){
                    
                    return true;
                }
            }
        }
        return false;
        
    }
    
    private Card addCards(int player, ArrayList<Card> table, boolean isBot)
    {
        if(!isBot){
            Card card = playerMove(player, players.get(player).isBot());
            if(card != null){
                for(Card tableCard : table){
                    if(tableCard.getValue() == card.getValue()){
                        players.get(player).removeCardFromHand(card);
                        System.out.println("Adding " + card.getMark() + card.getName());
                        return card;
                    }else{
                        System.out.println("Nothing to add");
                        return null;
                    }
                    
                }
            }
        }else{
            for(Card botCard : players.get(player).getPlayerHand()){
                for(Card cardOnTable : cards.getFloatPack()){
                    if(botCard.getName().equals(cardOnTable.getName())){
                        players.get(player).removeCardFromHand(botCard);
                        System.out.println("Adding " + botCard.getMark() + botCard.getName());
                        return botCard;
                    }
                    
                }
            }
        }
        return null;
    }
    
    private boolean checkIfCardCanDef(Card attCard, Card defCard)
    {
        if(defCard.getValue() > attCard.getValue() && defCard.getMark() == attCard.getMark()){
            return true;
        }else
            return defCard.getMark() == leadMark.getMark() && attCard.getMark() != leadMark.getMark();
    }
    
    private void setPlayersHands(int player)
    {
        for(int i = 0; i < player; i++){
            players.get(i).setPlayerHand(cards.getPackOfCards());
        }
    }
    
    private void setPlayers(int players, boolean isBot)
    {
        String name = "";
        if(player == 0){
            name = "Dana";
        }else if(player == 1){
            name = "Anton";
        }
        this.players.add(new Player(name + players, isBot));
        
    }
    
    private void putCardsOnTable(ArrayList<Card> cardsToPut)
    {
        for(Card card : cardsToPut){
            cards.putCardOnFloat(card);
        }
    }
    
    private void playerTakeCards(int player, ArrayList<Card> playerHand)
    {
        for(Card card : playerHand){
            players.get(player).addCardToHand(card);
        }
    }
    
    private void removeCardsFromPlayer(int player, ArrayList<Card> cardsToRemove)
    {
        for(Card card : cardsToRemove){
            players.get(player).removeCardFromHand(card);
        }
    }
    
    private boolean checkForWinner(ArrayList<Player> players)
    {
        for(Player player : players){
            if(player.getPlayerHand().size() == 0){
                System.out.println("Player -> " + player.getName() + " WINSSS!!!!");
                return true;
            }
        }
        return false;
    }
}

class waitForMe implements Runnable{
    
    @Override
    public void run()
    {
        try{
            Thread.sleep(500);
            
        }catch(InterruptedException e){
            e.getStackTrace();
        }
        
    }
}