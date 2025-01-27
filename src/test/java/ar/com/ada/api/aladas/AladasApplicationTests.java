package ar.com.ada.api.aladas;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.com.ada.api.aladas.entities.Aeropuerto;
import ar.com.ada.api.aladas.entities.Usuario;
import ar.com.ada.api.aladas.entities.Vuelo;
import ar.com.ada.api.aladas.entities.Vuelo.EstadoVueloEnum;
import ar.com.ada.api.aladas.security.Crypto;
import ar.com.ada.api.aladas.services.AeropuertoService;
import ar.com.ada.api.aladas.services.VueloService;
import ar.com.ada.api.aladas.services.AeropuertoService.ValidacionAeropuertoDataEnum;
import ar.com.ada.api.aladas.services.VueloService.ValidacionVueloDataEnum;

@SpringBootTest
class AladasApplicationTests {

	@Autowired
	VueloService vueloService;

	@Autowired
	AeropuertoService aeropuertoService;

	@Test
	void vueloTestPrecioNegativo() {

		Vuelo vueloConPrecioNegativo = new Vuelo();
		vueloConPrecioNegativo.setPrecio(new BigDecimal(-100));

		assertFalse(vueloService.validarPrecio(vueloConPrecioNegativo));
		// assertrue = afirmar que es verdadero
		// assertFalse = afirmar que no funciona

	}

	@Test
	void vueloTestPrecioOk() {

		Vuelo vueloConPrecioOk = new Vuelo();
		vueloConPrecioOk.setPrecio(new BigDecimal(100));

		assertTrue(vueloService.validarPrecio(vueloConPrecioOk));
		// assertrue = afirmar que es verdadero
		// assertFalse = afirmar que no funciona

	}

	@Test
	void aeropuertoValidarCodigoIATANoOk(){


		String codigoIATAOk1 = "EZE";
		String codigoIATAOk2 = "AEP";
		String codigoIATAOk3 = "NQN";
		String codigoIATAOk4 = "N  ";
		String codigoIATAOk5 = "N93";

		/*//Aca afirmo que espero que el leght del coso sea 3
		assertEquals(3, codigoIATAOk1.length());
		//Aca afirmo que espero que el resultado de la condicion
		//sea verdadera (en este caso leght() == 3)
		assertTrue(codigoIATAOk2.length() == 3);*/

		Aeropuerto aeropuerto1 = new Aeropuerto();
		aeropuerto1.setCodigoIATA(codigoIATAOk1);

		Aeropuerto aeropuerto2 = new Aeropuerto();
		aeropuerto2.setCodigoIATA(codigoIATAOk2);

		Aeropuerto aeropuerto3 = new Aeropuerto();
		aeropuerto3.setCodigoIATA(codigoIATAOk3);

		Aeropuerto aeropuerto4 = new Aeropuerto();
		aeropuerto4.setCodigoIATA(codigoIATAOk4);


		assertTrue(aeropuertoService.validarCodigoIATA(aeropuerto1));
		assertTrue(aeropuertoService.validarCodigoIATA(aeropuerto2));
		assertTrue(aeropuertoService.validarCodigoIATA(aeropuerto3));

		assertFalse(aeropuertoService.validarCodigoIATA(aeropuerto4));
		

	}

	@Test
	void aeropuertoValidarCodigoIATAOk(){

	}

	@Test
	void vueloVerificarValidacionAeropuertoOrigenDestino(){

	}

	@Test
	void chequearCapacidadQueLosPendientesNoTenganVuelosViejos(){
		// cuando se quieran hacer reservas de vuelo actuales no aparezcan viejos
	}

	@Test
	void  aeropuertoTestBuscadorIATA(){

	}

	@Test
	void vueloVerificarCapacidadMinima(){

	}

	@Test
	void vueloVerificarCapacidadMaxima(){
		
	}

	@Test
	void vueloValidarVueloMismoDestino(){
		Vuelo vuelo = new Vuelo();
		vuelo.setPrecio(new BigDecimal(10000));
		vuelo.setEstadoVueloId(EstadoVueloEnum.GENERADO);
		vuelo.setAeropuertoOrigen(116);
		vuelo.setAeropuertoDestino(116);

		assertEquals(ValidacionVueloDataEnum.ERROR_AEROPUERTOS_IGUALES, vueloService.validar(vuelo));

	}

	@Test
	void testearEncriptacion(){

		String contraImaginaria = "pitufoasesino";
		String contraImaginariaEncriptada = Crypto.encrypt(contraImaginaria, "palabra");

		String contraImaginariaEncriptadaDesencriptada =Crypto.decrypt(contraImaginariaEncriptada, "palabra");

		//assertTrue(contraImaginariaEncriptada.equals(contraImaginaria));
		assertEquals(contraImaginariaEncriptadaDesencriptada, contraImaginaria);

	}
    
	//como el test de abajo falla, volves a probar lo de arriba con los datos que usaste abajo
	@Test
	void testearContra(){
		Usuario usuario = new Usuario();
		usuario.setUsername("algo que pongas de la base de datos");
		usuario.setPassword("contra usuario que usaste arriba pero version encrip");
		usuario.setEmail("poner mail");

		assertFalse(!usuario.getPassword().equals(Crypto.encrypt("poner la que el usuario ingresa en postman", usuario.getEmail().toLowerCase())));
	}

	@Test
	void testearContraseña() {
		Usuario usuario = new Usuario();

		usuario.setUsername("Diana@gmail.com");
		usuario.setPassword("qp5TPhgUtIf7RDylefkIbw==");
		usuario.setEmail("Diana@gmail.com");

		assertFalse(!usuario.getPassword().equals(Crypto.encrypt("AbcdE23", usuario.getUsername().toLowerCase())));

	}

	@Test
	void testearAeropuertoId(){
		Aeropuerto aeropuerto = new Aeropuerto();
		aeropuerto.setAeropuertoId(117);
		aeropuerto.setCodigoIATA("MDZ");
		aeropuerto.setNombre("Mendoza");

		assertEquals(ValidacionAeropuertoDataEnum.ERROR_AEROPUERTO_YA_EXISTE, aeropuertoService.validar(aeropuerto));
	}


	@Test
	void testearAeropuertoCodigoIATA(){
		Aeropuerto aeropuerto = new Aeropuerto();
		aeropuerto.setAeropuertoId(17);
		aeropuerto.setCodigoIATA("  M");
		aeropuerto.setNombre("Mendoza");	

		assertEquals(ValidacionAeropuertoDataEnum.ERROR_CODIGO_IATA, aeropuertoService.validar(aeropuerto));
	}
}

	 


