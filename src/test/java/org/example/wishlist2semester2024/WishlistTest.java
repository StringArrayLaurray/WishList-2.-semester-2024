
import org.example.wishlist2semester2024.model.Wishlist;
import org.example.wishlist2semester2024.repository.WishRepository;

public class WishlistTest {

    public static void main(String[] args) {
        WishRepository repository = new WishRepository();

        // Brug en gyldig user_id fra din user-tabel (f.eks., 1, hvis du lige har indsat denne bruger)
        Wishlist newWishlist = new Wishlist("Rejseønsker", 1); // Opdater user_id til en gyldig værdi

        // Gem ønskelisten i databasen
        repository.saveWishlist(newWishlist);

        System.out.println("Ønskeliste gemt!");
    }
}

