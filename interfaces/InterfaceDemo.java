/**
 * Example demonstrating the usefulness of interfaces in Java
 * 
 * This code shows several key benefits of interfaces:
 * 1. Enabling polymorphism
 * 2. Supporting multiple implementation
 * 3. Defining contracts
 * 4. Enabling loose coupling
 * 5. Supporting the Strategy pattern
 */

// Payment Processor Example - interfaces enable different implementations
interface PaymentProcessor {
    boolean processPayment(double amount);
    void refundPayment(String transactionId, double amount);
    String getProcessorName();
}

// Different implementations of the PaymentProcessor interface
class CreditCardProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing $" + amount + " via Credit Card");
        // Credit card processing logic here
        return true;
    }

    @Override
    public void refundPayment(String transactionId, double amount) {
        System.out.println("Refunding $" + amount + " to Credit Card for transaction: " + transactionId);
        // Credit card refund logic here
    }

    @Override
    public String getProcessorName() {
        return "Credit Card";
    }
}

class PayPalProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing $" + amount + " via PayPal");
        // PayPal processing logic here
        return true;
    }

    @Override
    public void refundPayment(String transactionId, double amount) {
        System.out.println("Refunding $" + amount + " to PayPal account for transaction: " + transactionId);
        // PayPal refund logic here
    }

    @Override
    public String getProcessorName() {
        return "PayPal";
    }
}

class CryptoCurrencyProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing $" + amount + " via Cryptocurrency");
        // Crypto processing logic here
        return true;
    }

    @Override
    public void refundPayment(String transactionId, double amount) {
        System.out.println("Refunding $" + amount + " to Crypto wallet for transaction: " + transactionId);
        // Crypto refund logic here
    }

    @Override
    public String getProcessorName() {
        return "Cryptocurrency";
    }
}

// Order class that uses PaymentProcessor without needing to know the specific implementation
class Order {
    private String orderId;
    private double totalAmount;
    private PaymentProcessor paymentProcessor; // Reference to interface, not concrete class
    private String transactionId;

    public Order(String orderId, double totalAmount) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.transactionId = "TXN-" + orderId;
    }

    // Dependency injection through setter - can change payment processor at runtime
    public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public boolean checkout() {
        if (paymentProcessor == null) {
            throw new IllegalStateException("Payment processor not set");
        }
        
        System.out.println("Processing order " + orderId + " for $" + totalAmount);
        boolean success = paymentProcessor.processPayment(totalAmount);
        
        if (success) {
            System.out.println("Order " + orderId + " successfully processed with " + 
                               paymentProcessor.getProcessorName());
        }
        
        return success;
    }

    public void refund() {
        if (paymentProcessor == null) {
            throw new IllegalStateException("Payment processor not set");
        }
        
        System.out.println("Refunding order " + orderId);
        paymentProcessor.refundPayment(transactionId, totalAmount);
    }
}

// Multiple interface implementation example
interface Logger {
    void log(String message);
}

interface Encryptor {
    String encrypt(String data);
    String decrypt(String encryptedData);
}

// A class implementing multiple interfaces
class SecureLogger implements Logger, Encryptor {
    @Override
    public void log(String message) {
        System.out.println("Logging: " + message);
    }

    @Override
    public String encrypt(String data) {
        // Simple encryption logic for demo
        return "ENCRYPTED[" + data + "]";
    }

    @Override
    public String decrypt(String encryptedData) {
        // Simple decryption logic for demo
        if (encryptedData.startsWith("ENCRYPTED[") && encryptedData.endsWith("]")) {
            return encryptedData.substring(10, encryptedData.length() - 1);
        }
        return encryptedData;
    }
    
    public void secureLog(String message) {
        String encrypted = encrypt(message);
        log(encrypted);
    }
}

// Default method example in interfaces (Java 8+)
interface Vehicle {
    void start();
    void stop();
    
    // Default method - all implementing classes get this implementation automatically
    default void honk() {
        System.out.println("Beep beep!");
    }
    
    // Static method in interface (Java 8+)
    static void printVehicleInfo() {
        System.out.println("Vehicles are means of transportation");
    }
}

// Main class to demonstrate the examples
public class InterfaceDemo {
    public static void main(String[] args) {
        // Example 1: Payment processor with different implementations
        Order order = new Order("ORD-12345", 99.99);
        
        // Can easily switch between payment processors
        order.setPaymentProcessor(new CreditCardProcessor());
        order.checkout();
        
        order.setPaymentProcessor(new PayPalProcessor());
        order.checkout();
        order.refund();
        
        // Add new payment method without changing Order class
        order.setPaymentProcessor(new CryptoCurrencyProcessor());
        order.checkout();
        
        System.out.println("\n=== Strategy Pattern Example ===");
        
        // Example 2: Multiple interface implementation
        SecureLogger secureLogger = new SecureLogger();
        secureLogger.secureLog("Sensitive payment information");
        
        String encrypted = secureLogger.encrypt("Confidential data");
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + secureLogger.decrypt(encrypted));
        
        System.out.println("\n=== Interface as API Contract ===");
        // Use interface as type for polymorphism
        PaymentProcessor[] processors = {
            new CreditCardProcessor(),
            new PayPalProcessor(),
            new CryptoCurrencyProcessor()
        };
        
        // Process payments through all processors
        processPayments(processors, 50.0);
        
        // Example 3: Interface with default method
        System.out.println("\n=== Default Method Example ===");
        Car car = new Car();
        car.start();
        car.honk(); // Using default method from interface
        car.stop();
        
        // Static method on interface
        Vehicle.printVehicleInfo();
    }
    
    // Method using interface type for polymorphism
    public static void processPayments(PaymentProcessor[] processors, double amount) {
        for (PaymentProcessor processor : processors) {
            System.out.println("Using " + processor.getProcessorName() + ":");
            processor.processPayment(amount);
        }
    }
    
    // Simple class implementing Vehicle interface
    static class Car implements Vehicle {
        @Override
        public void start() {
            System.out.println("Car is starting...");
        }
        
        @Override
        public void stop() {
            System.out.println("Car is stopping...");
        }
        
        // Note: Car inherits the default honk() method from Vehicle
    }
}
