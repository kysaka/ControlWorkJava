import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ToyStore {
    private List<Toy> toys;
    private List<Toy> prizeToys;
    private int totalQuantity;

    public ToyStore() {
        toys = new ArrayList<>();
        prizeToys = new ArrayList<>();
        totalQuantity = 0;
    }

    public void addToy(int id, String name, int quantity, double weight) {
        Toy toy = new Toy(id, name, quantity, weight);
        toys.add(toy);
        totalQuantity += quantity;
    }

    public void changeWeight(int id, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == id) {
                toy.setWeight(newWeight);
                return;
            }
        }
        System.out.println("Toy with specified ID not found.");
    }

    public void drawToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        double randomValue = Math.random() * totalWeight;
        double currentWeight = 0;
    
        for (Toy toy : toys) {
            currentWeight += toy.getWeight();
            if (randomValue <= currentWeight && toy.getQuantity() > 0) {
                Toy selectedToy = new Toy(toy.getId(), toy.getName(), 1, toy.getWeight());
                toy.decreaseQuantity();
                totalQuantity--;
                prizeToys.add(selectedToy);
                saveToyToFileWithProbability(selectedToy, "winners.txt");
                System.out.println("Won toy: " + selectedToy.getName());
                return;
            }
        }
        System.out.println("All toys have been distributed.");
    }    

    public void saveToyToFile(Toy toy, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println("ID: " + toy.getId() + ", Name: " + toy.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();
        toyStore.addToy(1, "Ball", 10, 20);
        toyStore.addToy(2, "Doll", 15, 30);
        toyStore.addToy(3, "Car", 8, 10);

        toyStore.addToy(4, "Sword", 5, 15);
        toyStore.addToy(5, "Lego", 20, 25);
        toyStore.addToy(6, "Laptop", 2, 50);

    
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select an action:");
            System.out.println("1. Change toy weight");
            System.out.println("2. Draw a toy");
            System.out.println("3. Save quantity to file");
            System.out.println("4. Exit");
    
            int choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    System.out.println("Enter the ID of the toy you want to change:");
                    int toyId = scanner.nextInt();
                    System.out.println("Enter the new weight of the toy:");
                    double newWeight = scanner.nextDouble();
                    toyStore.changeWeight(toyId, newWeight);
                    break;
                case 2:
                    toyStore.drawToy();
                    break;
                case 3:
                    toyStore.saveQuantityToFile("Quantity.txt");
                    System.out.println("Quantity saved to Quantity.txt.");
                    break;
                case 4:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
    }

    public void saveToyToFileWithProbability(Toy toy, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println("ID: " + toy.getId() + ", Name: " + toy.getName() + ", Probability: " + toy.getWeight() + "%");
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Проверяем, сколько игрушек хранится в файле
        List<String> lines = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    
        // Если количество строк в файле превышает 10, удаляем старые записи
        if (lines.size() > 10) {
            List<String> lastTenLines = lines.subList(Math.max(lines.size() - 10, 0), lines.size());
    
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                for (String line : lastTenLines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

    public void saveQuantityToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Toy toy : toys) {
                writer.println("Name: " + toy.getName() + ", Quantity: " + toy.getQuantity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}