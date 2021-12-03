module com.example.taquin_fx {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;


	opens com.taquin_fx to javafx.fxml;
	exports com.taquin_fx;
}