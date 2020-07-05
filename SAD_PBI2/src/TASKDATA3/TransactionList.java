package TASKDATA3;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class TransactionList {
	// Transaction list
	private LinkedList<Transaction> transactionList;
	// Products present on the transactions
	private LinkedList<String> productList;
	// Months present on the transactions
	private LinkedList<String> monthList;

	// Constructor
	public TransactionList() {
		transactionList = new LinkedList<Transaction>();
		productList = new LinkedList<String>();
		monthList = new LinkedList<String>();
	}

	// Sort any LinkedList<String>
	private void sortStringLinkedList(LinkedList<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Collator.getInstance().compare(o1, o2);
			}
		});
	}

	// Add a month to the month list
	public void addMonth(String month) {
		if (!monthList.contains(month)) {
			monthList.add(month);
			sortStringLinkedList(monthList);
		}
	}

	// Add a product to the product list
	public void addProduct(String product) {
		if (!productList.contains(product)) {
			productList.add(product);
			sortStringLinkedList(productList);
		}
	}

	// Check if a transaction already exists
	public boolean transactionExists(String transactionMonth) {
		for (int i = 0; i < transactionList.size(); i++)
			if (transactionList.get(i).getMonth() == transactionMonth)
				return true;

		return false;
	}

	// Get a transaction position in the LinkedList
	public int getTransactionIndex(String transactionMonth) {
		for (int i = 0; i < transactionList.size(); i++)
			if (transactionList.get(i).getMonth() == transactionMonth)
				return i;

		return -1;
	}

	// Add transaction to the list
	public void addTransaction(Transaction transaction) {
		if (transactionExists(transaction.getMonth())) {
			int transactionIndex = getTransactionIndex(transaction.getMonth());

			// Add the products to the existing transaction
			for (int i = 0; i < transaction.getProducts().size(); i++) {
				transactionList.get(transactionIndex).addProduct(transaction.getProducts().get(i));
				addProduct(transaction.getProducts().get(i));
			}
		} else {
			transactionList.add(transaction);
			addMonth(transaction.getMonth());
			for (int i = 0; i < transaction.getProducts().size(); i++) {
				addProduct(transaction.getProducts().get(i));
			}
		}
	}

	// Remove transaction from the list
	public void removeTransaction(Transaction transaction) {
		transactionList.remove(transaction);
	}

	// Return the data has arff String
	public String toARFF() {
		String arff = "";

		arff += "@relation TASKDATA3\n\n"; // Add TaskData relation name (in this case it is the TaskData 3

		// Add the months to the attribute list
		arff += "@attribute Months {";
		// Add the transactions to the possible values list
		for (int i = 0; i < monthList.size(); i++) {
			arff += monthList.get(i) + ",";
		}
		// Remove last comma and close the transaction attribute
		arff = arff.substring(0, arff.length() - 1);
		arff += "}\n";

		// Add the products to the attribute list
		for (int i = 0; i < productList.size(); i++) {
			arff += "@attribute " + productList.get(i) + " {1,0}\n";
		}

		// Add the data (transactions)
		arff += "\n@data\n";
		for (int i = 0; i < transactionList.size(); i++) {
			Transaction currentTransaction = transactionList.get(i);

			arff += currentTransaction.getMonth() + ",";

			// Set the products
			for (int a = 0; a < productList.size(); a++) {
				if (currentTransaction.getProducts().contains(productList.get(a)))
					arff += "1,";
				else
					arff += "0,";
			}
			// Remove extra comma
			arff = arff.substring(0, arff.length() - 1);
			arff += "\n";
		}

		return arff;
	}
}
