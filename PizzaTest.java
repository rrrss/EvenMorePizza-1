import java.io.*;
import java.util.*;


class Pizza implements Comparable<Pizza> {
    public int ingredient;
    public HashSet<String> arr;
    public static int counter = 0;
    public int id;

    public Pizza(int ingredient, String[] array) {
        this.ingredient = ingredient;
        this.arr = new HashSet<>();
        for (String i : array)
            this.arr.add(i);
        this.id = counter++;
    }

    public Pizza(Pizza p) {
        this.ingredient = p.ingredient;
        this.arr = new HashSet<>();
        this.arr.addAll(p.arr);
        this.id = p.id;
    }

    @Override
    public int compareTo(Pizza o) {
        if (this.ingredient > o.ingredient) return -1;
        if (this.ingredient < o.ingredient) return 1;
        return 0;
    }

    public int compareTo2(Pizza o) {
        if (this.ingredient > o.ingredient) return 1;
        if (this.ingredient < o.ingredient) return -1;
        return 0;
    }
}

class Pizzas {

    public ArrayList<Pizza> set;

    public Pizzas() {
        this.set = new ArrayList<>();
    }

    public Pizzas deepCopy() {
        Pizzas p = new Pizzas();
        for (Pizza pizza : this.set) {
            p.set.add(new Pizza(pizza));
        }
        return p;
    }

    public Pizza getPizza(int id) {
        for (Pizza p : set) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }
}


public class PizzaTest {

    public PizzaTest() {
    }

    public static int Team2 = 0;
    public static int Team3 = 0;
    public static int Team4 = 0;

    public static int PizzaNum = 0;

    public static int totalTeams = 0;

    public static Pizzas pizzasG = new Pizzas();
    public static Pizzas pizzasGTemp = new Pizzas();

    //public static ArrayList<Pizza> pizzas = new ArrayList<>();
    public static void ReadFile(String filename) {
        try {
            File myObj = new File("src/"+filename);
            Scanner myReader = new Scanner(myObj);
            String[] info = myReader.nextLine().split(" ");
            PizzaNum = Integer.parseInt(info[0]);
            Team2 = Integer.parseInt(info[1]);
            Team3 = Integer.parseInt(info[2]);
            Team4 = Integer.parseInt(info[3]);

            for (int i = 0; i < PizzaNum; i++) {
                String data = myReader.nextLine();
                String[] dataSplited = data.split(" ");
                int ingredient = Integer.parseInt(dataSplited[0]);

                Pizza p = new Pizza(ingredient, Arrays.copyOfRange(dataSplited, 1, dataSplited.length));
                pizzasG.set.add(p);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static Pizza getMost(Pizzas pizzas) {
        Pizza most = pizzas.set.remove(0);
        pizzas.set.sort(Pizza::compareTo2);
        for (Pizza p : pizzas.set) {
            removeDup(most, p);

        }
        return most;
    }


    public static void removeDup(Pizza p1, Pizza p2) {
        for (String Tosefet : p1.arr) {
            if (p2.arr.contains(Tosefet)) {
                //p2.arr.remove(Tosefet);
                p2.ingredient--;
            }
        }
    }

    public static int getScoreFromDelivery(ArrayList<Pizza> pizzas) {
        int score = 0;
        HashSet<String> bigSet = new HashSet<>();
        for (Pizza p : pizzas) {
            bigSet.addAll(p.arr);
        }

        return bigSet.size() * bigSet.size();
    }

    public static ArrayList<Pizza> bestOf(int size) {
        try {
            ArrayList<Pizza> delivery = new ArrayList<>();
            Pizzas newP = pizzasG.deepCopy(); //Orginal
            for (int j = 0; j < size; j++) {
                newP.set.sort(Pizza::compareTo);
                Pizza most = getMost(newP);
                delivery.add(pizzasG.getPizza(most.id));
            }
            return delivery;
        }
        catch (Exception e){
            return null;
        }
    }


    public static void WriteToFile(String filename , ArrayList<StringBuilder> strings) throws IOException {
        FileWriter f0 = new FileWriter(new File(filename));


        for(int i=0;i<strings.size();i++)
        {
            f0.write(String.valueOf(strings.get(i)));
            f0.write("\n");
        }
        f0.close();


    }


    public static void main(String[] args) {
        if (args.length > 0) {


            String filename = args[0];

            ReadFile("input/"+filename);
            //StartDelivery();
            pizzasGTemp = pizzasG.deepCopy();
            int size = PizzaNum;
            ArrayList<StringBuilder> allLines = new ArrayList<>();


            int score = 0;
            System.out.println(size);


            while (size > 1) {
                if(Team4==0 && Team2==0 && Team3==0){
                    break;
                }
                int scoreteam4 = -1;
                int scoreteam3 = -1;
                int scoreteam2 = -1;
                ArrayList<Pizza> team4 = bestOf(4);
                ArrayList<Pizza> team3 = bestOf(3);
                ArrayList<Pizza> team2 = bestOf(2);

                if(team4!=null&& Team4>0)
                     scoreteam4 = getScoreFromDelivery(team4);
                if(team3!=null&& Team3>0)
                     scoreteam3 = getScoreFromDelivery(team3);
                if(team2!=null&& Team2>0)
                     scoreteam2 = getScoreFromDelivery(team2);

                int best = Math.max(scoreteam3, Math.max(scoreteam2, scoreteam4));
                StringBuilder answer = new StringBuilder();
                if (best == scoreteam2 && Team2>0) {
                    answer.append(2);
                    for (Pizza p : team2) {
                        pizzasG.set.remove(pizzasG.getPizza(p.id));
                        answer.append(" ");
                        answer.append(p.id);
                    }
                    Team2--;
                    score += scoreteam2;
                    size -= 2;
                } else if (best == scoreteam3 && Team3>0) {
                    answer.append(3);
                    for (Pizza p : team3) {
                        pizzasG.set.remove(pizzasG.getPizza(p.id));
                        answer.append(" ");
                        answer.append(p.id);
                    }
                    score += scoreteam3;
                    size -= 3;
                    Team3--;
                } else if  (Team4>0){

                        answer.append(4);
                        for (Pizza p : team4) {

                            pizzasG.set.remove(pizzasG.getPizza(p.id));
                            answer.append(" ");
                            answer.append(p.id);
                        }
                        score += scoreteam4;

                        size -= 4;
                        Team4--;

                }
                totalTeams++;
                allLines.add(answer);
            }

            allLines.add(0,new StringBuilder(String.valueOf(totalTeams)));


            System.out.println(score);
            try {
                WriteToFile(filename +".out", allLines);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("End");
        }
    }
}
