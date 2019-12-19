package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {
  public static void main(String[] args) {
    SellerDao sellerDao = DaoFactory.createSellerDao();
    System.out.println("=== TEST 1: Seller findById ===");
    System.out.println(sellerDao.findById(3));
    System.out.println("===============================");

  }
}
