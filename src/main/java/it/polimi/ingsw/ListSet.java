package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains static methods used to operate with set operations
 */
public class ListSet {
	
	/**
	 * check if subList is strictly contained in set. Duplicate elements are allowed and treated as distinct objects
	 * @param <T> generic type: this function supports generic arraylists as parameters
	 * @param set the list of T elements representing the greater set, in which subList may be contained in
	 * @param subList the list of T elements representing the subset of set
	 */
	public static <T> boolean subset(ArrayList<T> set, ArrayList<T> subList) {
		// hashmap for subset, maps every object to its multiplicity
		HashMap<T, Integer> subSetMap = new HashMap<>(10);
		for (T obj: subList) {
			subSetMap.put(obj, subSetMap.getOrDefault(obj, 0) + 1);
		}
		// hashmap for set, maps every object to its multiplicity
		HashMap<T, Integer> setMap = new HashMap<>(10);
		for (T obj: set) {
			setMap.put(obj, setMap.getOrDefault(obj, 0) + 1);
		}
		
		boolean check = setMap.keySet().containsAll(subSetMap.keySet());
		if (!check) return false; // if the set doesn't contain every object type of the list subset, returns false
		
		// calculates the difference of the elements and their values between the set and the subset
		setMap.forEach((t, x) -> setMap.replace(t, x - subSetMap.getOrDefault(t, 0)));
		
		// checks whether the setMap contains negative values: if it finds them, returns false (true otherwise).
		return setMap.values().stream().noneMatch(x -> x < 0);
	}
	
	/**
	 * create function to calculate the number of times a certain element appears in a single list
	 * @param set the list of generic elements
	 * @param element the generic element to find in the list
	 * @param <T> generic type for the list of elements
	 * @return the number of occurrences of a certain element in the list
	 */
	public static <T> int count(ArrayList<T> set, T element) {
		int count = 0;
		for (T t: set) {
			if (element.equals(t)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * takes a set of generic elements in input, and removes the subset from it, while making distinction among duplicates
	 * @param <T> the generic type of elements in the lists
	 * @param set the set of generic input elements
	 * @param subsetToBeRemoved the list of generic elements to be removed from the set
	 */
	public static <T> ArrayList<T> removeSubSet(ArrayList<T> set, ArrayList<T> subsetToBeRemoved) {
		// hashmap for set, maps every object to its multiplicity
		HashMap<T, Integer> setMap = new HashMap<>(10);
		for (T obj: set) {
			setMap.put(obj, setMap.getOrDefault(obj, 0) + 1);
		}
		// hashmap for set, maps every object to its multiplicity
		HashMap<T, Integer> removalSet = new HashMap<>(10);
		for (T obj: subsetToBeRemoved) {
			removalSet.put(obj, removalSet.getOrDefault(obj, 0) + 1);
		}
		
		// calculates the difference of the elements and their values between the set and the subset
		setMap.forEach((t, x) -> setMap.replace(t, x - removalSet.getOrDefault(t, 0)));
		
		// creates a new set with the difference between the two sets
		ArrayList<T> finalSet = new ArrayList<>();
		setMap.forEach((t, x) -> {
			for (int i = 0; i < x; i++) {
				finalSet.add(t);
			}
		});
		return finalSet;
	}
	
	public static <T> HashMap<T, Integer> multiplicityList(ArrayList<T> list) {
		HashMap<T, Integer> setMap = new HashMap<>(10);
		for (T obj: list) {
			setMap.put(obj, setMap.getOrDefault(obj, 0) + 1);
		}
		return setMap;
	}
	
	public static <T> void showHashMap(HashMap<T, Integer> map) {
		map.forEach((t, x) -> {
			System.out.print(x + "x" + t.toString() + "\t");
		});
		System.out.print("\n");
	}
	
}
