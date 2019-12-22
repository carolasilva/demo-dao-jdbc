package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    SellerDao sellerDao = DaoFactory.createSellerDao();
    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    System.out.println();
    System.out.println("=== TEST 1: Seller findById ===");
    System.out.println(sellerDao.findById(3));
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 2: Seller findByDepartment ===");
    Department department = new Department(2, null);
    List<Seller> sellers = sellerDao.findByDepartment(department);
    for (Seller seller : sellers)
      System.out.println(seller);
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 3: Seller findAll ===");
    sellers = sellerDao.findAll();
    for (Seller seller : sellers)
      System.out.println(seller);
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 4: Seller insert ===");
    Seller seller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
    sellerDao.insert(seller);
    System.out.println("Inserted! New id = " + seller.getId());
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 5: Seller update ===");
    seller = sellerDao.findById(1);
    seller.setName("Carol");
    sellerDao.update(seller);
    System.out.println("Update completed");
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 6: Seller delete ===");
    System.out.println("Enter id for delete test: ");
    int id = scanner.nextInt();
    sellerDao.deleteById(id);
    System.out.println("Delete completed");
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 7: Department insert ===");
    department = new Department(null, "Teste");
    departmentDao.insert(department);
    System.out.println("Inserted! New id = " + department.getId());
    System.out.println("===============================");

    scanner.close();

  }
}
