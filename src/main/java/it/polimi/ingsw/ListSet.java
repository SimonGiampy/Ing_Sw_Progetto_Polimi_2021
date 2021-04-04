package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * This class contains static methods used to operate with set operations
 */
public class ListSet {
	
	/**
	 * check if subList is strictly contained in set. Duplicate elements are allowed and treated as distinct objects
	 * @param subList the list of T elements representing the subset of set
	 * @param set the list of T elements representing the greater set, in which subList may be contained in
	 * @param <T> generic type: this function supports generic arraylists as parameters
	 */
	public static <T> boolean subset(ArrayList<T> subList, ArrayList<T> set) {
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
	
	//TODO: create function to calculate the number of times a certain element appears in a single list
	public static <T> void count(ArrayList<T> set, T element) {
	
	}
}
