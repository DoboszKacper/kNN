import Models.DistanceComparator;
import Models.Flower;
import Models.Result;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class kNN {

    private static int k;

    //List of test data
    private static List<double[]> test = new ArrayList<double[]>();

    //List to save flower data
    private static List<Flower> flowerList = new ArrayList<Flower>();

    //List to save distance result
    private static List<Result> resultList = new ArrayList<Result>();

    //List of flower names from test file
    private static List<String> flowerNamesList = new ArrayList<>();

    //List of flower names from result
    private static List<String> flowerNamesListResult = new ArrayList<>();

    //Outcome
    private static int numberOfMatchingStrings=0;

    public static void main(String[] args) {
        //Assign k
        k = Console(); // of neighbours

        //Reading given files
       ReadFileTraining("src\\Data\\iris_training.txt");
       ReadFileTest("src\\Data\\iris_test.txt");

        for (int i = 0; i <test.size() ; i++) {
        flowerNamesListResult.add(algorithm(test.get(i)));
        }


        if(flowerNamesList.size()==flowerNamesListResult.size()){
            for (int i = 0; i <flowerNamesListResult.size() ; i++) {
                if(flowerNamesListResult.get(i).contains(flowerNamesList.get(i))){
                    numberOfMatchingStrings++;
                }
            }
        }else{
            System.out.println("Error lists are not the same length!!!");
        }

        float percent = (float) (numberOfMatchingStrings*100)/flowerNamesListResult.size();
        System.out.println("Liczba prawidłowo zaklasyfikowanych przykładów to: "+ numberOfMatchingStrings + ", natomiast prawidłowość w procentach to: "+percent+"%");



    }


    public static String algorithm(double[] array){
        //Find distances
        for (Flower flower : flowerList) {
            double dist = 0.0;
            for (int i = 0; i < flower.FlowerAttributes.length; i++) {
                dist += Math.pow(flower.FlowerAttributes[i] - array[i], 2);
            }
            double distance = Math.sqrt(dist);
            resultList.add(new Result(distance, flower.FlowerName));
        }

        //Sort distances
        Collections.sort(resultList, new DistanceComparator());
        String[] ss = new String[k];
        for (int i = 0; i < k; i++) {

            ss[i] = resultList.get(i).FlowerName;
        }


        String majClass = findMajorityClass(ss);
        resultList.clear();
        return majClass;
    }

    public static String findMajorityClass(String[] array){
        Set<String> a = new HashSet<String>(Arrays.asList(array));
        //Convert the HashSet back to array
        String[] uniqueValues = a.toArray(new String[0]);
        //Counts for unique strings
        int[] counts = new int[uniqueValues.length];
        //loop thru unique strings and count how many times they appear in original array
        for (int i = 0; i <uniqueValues.length ; i++) {
            for (int j = 0; j <array.length; j++) {
                if(array[j].equals(uniqueValues[i])){
                    counts[i]++;
                }
            }
        }

        int max = counts[0];
        for (int counter = 1; counter < counts.length; counter++) {
            if(counts[counter] > max)
                max = counts[counter];
        }

        //how many times max appears
        int freq = 0;
        for (int counter = 0; counter < counts.length; counter++) {
            if(counts[counter] == max)
                freq++;
        }

        //index of most freq value if we have only one mode
        int index = -1;
        if (freq==1){
            for (int counter = 0; counter < counts.length; counter++) {
                if(counts[counter] == max) {
                    index = counter;
                    break;
                }
            }

            return uniqueValues[index];
        }else {//we have multiple modes
            int[] ix = new int[freq]; // array of indices of modes
            System.out.println("multiple majority classes: "+ freq+ " classes");
            int ixi = 0;
            for (int counter = 0; counter < counts.length; counter++) {
                if(counts[counter] == max) {
                    ix[ixi] = counter; // save index of each max count value
                    ixi++; //increase index of ix array
                }
            }

            for (int counter = 0; counter < counts.length; counter++) {
                System.out.println("class index: " + ix[counter]);
            }

            //now choose one at random
            Random g = new Random();
            //get random number 0 <= rIndex < size of ix
            int rIndex = g.nextInt(ix.length);
            System.out.println("random index: " + rIndex);
            int nIndex = ix[rIndex];
            //return unique value at that index
            return uniqueValues[nIndex];
        }
    }

    public static int Console(){
        System.out.print("Podaj k: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static void ReadFileTest(String path){
        File f1 = new File(path);
        try (Scanner sc = new Scanner(f1)) {
            while (sc.hasNextLine()) {

                String line[] = sc.nextLine()
                        .replaceAll(",",".")
                        .split("\\s+");

                String flowerName = line[line.length-1];
                double numbers[] = new double[line.length-2];

                for (int i = 0; i <numbers.length ; i++) {
                    numbers[i] = Double.valueOf(line[i+1]);
                }
                test.add(numbers);
                flowerNamesList.add(flowerName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void ReadFileTraining(String path){
        File f2 = new File(path);
        try (Scanner sc = new Scanner(f2)) {
            while (sc.hasNextLine()) {
                String line[] = sc.nextLine().replaceAll(",",".").split("\\s+");

                Double numbers[] = new Double[line.length-2];
                for (int i = 0; i <numbers.length ; i++) {
                    numbers[i] = Double.valueOf(line[i+1]);
                }
                flowerList.add(new Flower(numbers,line[line.length-1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
