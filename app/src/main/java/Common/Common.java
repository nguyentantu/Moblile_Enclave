package Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.Person;

public class Common {
    public static final int VIEWTYPE_GROUP = 0;
    public static final int VIEWTYPE_PERSON = 1;
    public static final int RESULT_CODE = 1000;
    public static List<String> alphabet_available = new ArrayList<>();

    // Function to sort person list name by alphabet
    public static ArrayList<Person> sortList(final ArrayList<Person> people){
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person person, Person t1) {
                return person.getName().compareTo(t1.getName());
            }
        });
        return people;
    }

    // After sorted, this function will add alphabet character  to list
    public static ArrayList<Person> addAlphabet(ArrayList<Person> list){
        int i = 0;
        ArrayList<Person> customList = new ArrayList<>();
        Person firstPosition = new Person();
        firstPosition.setName(String.valueOf(list.get(0).getName().charAt(0)));
        firstPosition.setViewType(VIEWTYPE_GROUP);
        alphabet_available.add(String.valueOf(list.get(0).getName().charAt(0))); // add first character to group header list

        customList.add(firstPosition);
        for (i = 0; i < list.size() -1 ; i++){
            Person person = new Person();
            char name1 = list.get(i).getName().charAt(0); // set first character in name
            char name2 = list.get(i+1).getName().charAt(0);
            if (name1 == name2){
                list.get(i).setViewType(VIEWTYPE_PERSON);
                customList.add(list.get(i));
            } else {
                list.get(i).setViewType(VIEWTYPE_PERSON);
                customList.add(list.get(i));
                person.setName(String.valueOf(name2));
                person.setViewType(VIEWTYPE_GROUP);
                alphabet_available.add(String.valueOf(name2));
                customList.add(person);
            }
        }

        list.get(i).setViewType(VIEWTYPE_PERSON);
        customList.add(list.get(i));
        return customList;
    }

    // this function with return position of string in list
    public static int findPositionWithName(String name, ArrayList<Person> list){
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1; // -1 if not found
    }

    // This function will generate an Alphabet list
    public static ArrayList<String> genAlphabet(){
        ArrayList<String> result = new ArrayList<>();
        for (int i = 65; i <= 90; i++){
            char character =  (char)i;
            result.add(String.valueOf(character));;
        }
        return result;
    }

}
