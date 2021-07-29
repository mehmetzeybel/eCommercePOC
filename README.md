# eCommercePOC
E-commerce REST API PoC kurulum ve çalıştırma adımları aşağıdaki gibidir.

## Kurulum
1. Proje kodu github'dan klonlanır.
2. Eclipse IDE
   * Import Projects -> Maven -> Existing Maven Project olarak import edilir.
   * Java Library olarak **JDK** kullanılmalıdır.
   * lombok plugin kurulu değilse  
    `.m2\repository\org\projectlombok\lombok\1.18.20\lombok-1.18.20.jar` çalıştırılarak kurulum yapılır.
   * Eclipse IDE yeniden başlatılır ve projenin rebuild olması beklenir.
3. Intellij Idea
   * File -> Open ile projenin bulunduğu klasör seçilir Open As seçiminde Maven seçilir.
   * lombok plugin kurulu değilse Plugins altından lombok plugin'i kurulur ardından yeniden başlatılır.

## Çalıştırma
1. application.properties içerisindeki satırların karşılıkları değiştirilmelidir. Geçerli bir url ve DB kullanıcı bilgileri yazılmalıdır.  
`spring.datasource.url=`  
`spring.datasource.username=  `  
`spring.datasource.password=  `   
2. Eclipse IDE 
  * Run -> Run Configurations -> Main Class olarak : PasajApplication.java seçilir.
4. Intellij Idea
  * Run arayüzünden PasajApplication çalıştırılır. 

## Swagger UI
Swagger UI adresi'ne http://localhost:8080/swagger-ui.html url'inden erişilir.

## Unit Testlerin Çalıştırılması
1. Eclipse IDE
  * src/test/java/com/turkcell/pasaj/PropertyRestApiTest.java üzerinden Run As -> Run Configurations -> Test sekmesi altındaki Test runner: JUnit 4 olarak seçilip testler çalıştırılır.
2. Intellij Idea 
  * PropertyRestApiTest çalıştırılır.
