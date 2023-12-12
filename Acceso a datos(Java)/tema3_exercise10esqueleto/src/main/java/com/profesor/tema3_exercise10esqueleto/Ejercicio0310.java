package com.profesor.tema3_exercise10esqueleto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;



public class Ejercicio0310 {
	public static Scanner sc = new Scanner(System.in);
	
	public static void main (String[] args) throws ClassNotFoundException, SQLException{
		ManejadorBaseDatos manejador = new ManejadorBaseDatos();
		
		boolean exit = false;
		
		do
		{
			ShowMenu();
			switch (getOption()) {
			case 1:
				addPersona(manejador);
				break;
			case 2:
				addCliente(manejador);		
				break;
			case 3:
				addFuncionario(manejador);			
				break;
			case 4:
				modifyPersona(manejador);			
				break;
			case 5:
				modifyCliente(manejador);			
				break;
			case 6:
				modifyFuncionario(manejador);			
				break;
			case 7:
				showPersonas(manejador);			
				break;
			case 8:
				showClientes(manejador);			
				break;
			case 9:
				showFuncionarios(manejador);			
				break;
			case 0:
				exit = true;
				break;
			default:
				break;
			}
		}
		while(!exit);
	}
	
	private static int getOption() {
		System.out.println("Option:");
		int option = sc.nextInt();
		sc.nextLine();
		return option;
	}

	private static void ShowMenu()
	{
		System.out.println("1.-Añadir persona");
		System.out.println("2.-Añadir cliente");
		System.out.println("3.-Añadir funcionario");
		System.out.println("4.-Modificar persona");
		System.out.println("5.-Modificar cliente");
		System.out.println("6.-Modificar funcionario");
		System.out.println("7.-Ver personas");
		System.out.println("8.-Ver clientes");
		System.out.println("9.-Ver funcionarios");
		System.out.println("0.-Salir");
	}
	
	
	private static void addPersona(ManejadorBaseDatos manejador) 
			throws ClassNotFoundException, SQLException 
		{
			try{
			//pido los datos de la persona
			System.out.println("Añadimos una persona");
			System.out.println("Nombre de la persona:");
			String nombre = sc.nextLine();
			System.out.println("Apellidos de la persona:");
			String apellidos = sc.nextLine();
			System.out.println("Introduce direccion:");
			String direccion= sc.nextLine();
			System.out.println("Ponga un telefono:");
			String telefonoSr= sc.nextLine();
			int telefono = Integer.parseInt(telefonoSr);

			System.out.println("Introduce fecha de nacimiento (dd/MM/yyyy): ");
			String fechaInput= sc.nextLine();
			sc.nextLine();
			SimpleDateFormat formato= new SimpleDateFormat("dd/MM/yyyy");
		
			//a continuacion ingreso los datos en la base de datos
			try{
				Date fecha= formato.parse(fechaInput);
			
				String sql= "INSERT INTO personas(nombre, apellidos, direccion, telefono, fecha_nacim) VALUES (?,?,?,?,?)";
				try(Connection con = manejador.conection();
					PreparedStatement preparedStatement= con.prepareStatement(sql)){
					preparedStatement.setString(1, nombre );
					preparedStatement.setString(2, apellidos);
					preparedStatement.setString(3, direccion);
					preparedStatement.setInt(4, telefono);
					preparedStatement.setDate(5, new java.sql.Date(fecha.getTime()));
					preparedStatement.executeUpdate();
					System.out.println("Persona agregada con éxito!");
				}
				
			}catch (ParseException e) {
				System.out.println("Error al guardar");
			}
				}catch (Exception e) {
				System.out.println("Error al introducir los datos");
			}
		}
	private static void addCliente(ManejadorBaseDatos manejador) 
		throws ClassNotFoundException, SQLException 
	{
		try{
			System.out.println("Pon el numero de Cuenta: ");
			String numeroCuenta= sc.nextLine();
			System.out.println("Introduce el estado (Activo, Pendiente, Inactivo):");
			String palabra = sc.nextLine();
			String[] palabrasClave = {"Activo", "Pendiente", "Inactivo"};
				if (Arrays.asList(palabrasClave).contains(palabra)) {
					System.out.println("Cumple las condiciones");
				} else {
					System.out.println("Lo has puesto incorrecto, se quedará en Pendiente");
					palabra= "Pendiente";
			}

			System.out.println("Introduce el tipo de cliente que eres (Normal o Premium): ");
			List<String> TiposdeCliente = Arrays.asList("Normal", "Premium");
			String clienteString= sc.nextLine();
			while (true) {
				if(TiposdeCliente.contains(clienteString)){
					System.out.println("Tipo Correcto");
					break;
				}else {
					System.out.println("Tipo Incorrecto vuelve a intertarlo");
					System.out.println("Introduce el tipo de cliente que eres: ");
					clienteString= sc.nextLine();
				}
				sc.nextLine(); 
			}
			try {
				String sql= "INSERT INTO clientes(nroCuenta, estado, tipoCliente)VALUES (?,?,?)";
				try(Connection con = manejador.conection();
					PreparedStatement preparedStatement = con.prepareStatement(sql)){
						preparedStatement.setString(1, numeroCuenta);
						preparedStatement.setString(2, palabra);
						preparedStatement.setString(3, clienteString);
						preparedStatement.executeUpdate();
						System.out.println("Agregado el cliente correctemente");
					}
			} catch (Exception e) {
				System.out.println("Error al guardar en la base de datos");
				e.printStackTrace(); 
			}
		}catch(Exception e)
		{
			System.out.println("Error en las variables");
		}

	}
	
	private static void addFuncionario(ManejadorBaseDatos manejador) 
		throws ClassNotFoundException, SQLException 
	{
		try{
			System.out.println("Introduce los campos del numero de cuenta:");
			System.out.println("Dime el grupo al que pertenece: ");
			String grupoString= sc.nextLine();
			System.out.println("Dime el codigo del numero al que esta asignado: ");
			String codigoString= sc.nextLine();
			System.out.println("Diga el departametno al que procede");
			String departamentoString= sc.nextLine();
			System.out.println("Ingrese la fecha de cuando empezo a trabajar(dd/MM/yyyy): ");
			String fecha_ingreso= sc.nextLine();
			SimpleDateFormat format= new SimpleDateFormat("dd/mm/yyyy");
			try{
				
				String sql= "INSERT INTO datoCompuesto(grupo, codigo)VALUES (?,?)";
				try(Connection con = manejador.conection();
				PreparedStatement preparedStatement= con.prepareStatement(sql)){
					preparedStatement.setString(1, grupoString);
					preparedStatement.setString(2, codigoString);
					preparedStatement.executeUpdate();
					System.out.println("Agregado los datos1 correctamente");
				}
			}catch(Exception e){
				System.out.println("Error al guardar los datos1");
			}
			try {
				Date fecha= format.parse(fecha_ingreso);
				String sql2= "INSERT INTO Funcionarios(departamento, fecha_ingreso) VALUES (?,?) ";
				try(Connection con= manejador.conection();
				PreparedStatement preparedStatement= con.prepareStatement(sql2)){
					preparedStatement.setString(1, departamentoString);
					preparedStatement.setDate(2, new java.sql.Date(fecha.getTime()));
					preparedStatement.executeUpdate();
					System.out.println("Agregado los datos2 correctamete");
				}
				
			} catch (ParseException e) {
				System.out.println("Error al guardar los datos2 correctamente");
			}
		}catch(Exception e){
			System.out.println("Error en las variables");
		}

	}
	
	private static void modifyPersona(ManejadorBaseDatos manejador) 
		throws ClassNotFoundException, SQLException 
	{
		int opcion= 0;
		int codigo =0;
		System.out.println("Introduce el numero de la persona que quieras modificar");
		try{
		codigo = sc.nextInt();
		sc.nextLine(); // Consumir el carácter de nueva línea pendiente
		}catch(Exception e){
			System.out.println("Error en el tipo de varaible");
		}
		System.out.println("Seleccione los campos que desea modificar:");
		System.out.println("1. Nombre");
		System.out.println("2. Apellido");
		System.out.println("3. Dirección");
		System.out.println("4. Telefono");
		System.out.println("5. Fecha de Nacimiento");
		try{
		opcion = sc.nextInt();
		sc.nextLine(); // Consumir el carácter de nueva línea pendiente
		}catch(Exception e ){
			System.out.println("Error ");
		}
		
	
		switch (opcion) {
				case 1:
				try{
					System.out.println("Introduce el nuevo nombre");
					String nuevoNombre= sc.nextLine(); 
					if (!nuevoNombre.isEmpty()) {
						try {
							String sql = "UPDATE personas SET nombre = ? WHERE numero = ?";
							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setString(1, nuevoNombre);
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizado el nombre");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar");
						}
					} else {
						System.out.println("No se ha introducido un nuevo nombre. La base de datos no se actualizará.");
					}
				}catch(Exception e)
				{
					System.out.println("Error en la variable");
				}
					
					break;
				case 2:
				try{
						System.out.println("Introduce el nuevos Apellidos");
					String nuevoApellido= sc.nextLine(); 
					if (!nuevoApellido.isEmpty()) {
						try {
							String sql = "UPDATE personas SET apellidos = ? WHERE numero = ?";
							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setString(1, nuevoApellido);
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizado el apellido");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar");
						}
					} else {
						System.out.println("No se ha introducido un nuevo apellido. La base de datos no se actualizará.");
					}
				}catch(Exception e)
				{
					System.out.println("Error en variables");
				}
					break;
				case 3:
				try{
						System.out.println("Introduce el nueva direccion");
					String nuevoDireccion= sc.nextLine(); 
					if (!nuevoDireccion.isEmpty()) {
						try {
							String sql = "UPDATE personas SET direccion = ? WHERE numero = ?";
							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setString(1, nuevoDireccion);
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizado la direccion");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar");
						}
					} else {
						System.out.println("No se ha introducido una nueva direccion. La base de datos no se actualizará.");
					}
				}catch(Exception e){
					System.out.println("Error de Variables");
				}
					break;
				case 4:
				try{
					System.out.println("Introduce el nuevo Telefono");
					int nuevoTelefono= sc.nextInt(); 
					String nuevoTelefonoStr = String.valueOf(nuevoTelefono);
					if (!nuevoTelefonoStr.isEmpty()) {
						try {
							String sql = "UPDATE personas SET telefono = ? WHERE numero = ?";
							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setString(1, nuevoTelefonoStr);
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizado el telefono");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar");
						}
					} else {
						System.out.println("No se ha introducido un nuevo telefono. La base de datos no se actualizará.");
					}
						break;
				}catch (Exception e){
					System.out.println("Error en variables");
				}
				case 5:
				try{
					System.out.println("Introduce la nueva fecha de nacimiento (Formato: dd/MM/yyyy): ");
					String nuevaFechaNacimientoStr = sc.nextLine();

					// Verificar si nuevaFechaNacimientoStr no está vacía antes de realizar alguna acción
					if (!nuevaFechaNacimientoStr.isEmpty()) {
						try {
							// Convertir la cadena de fecha a un objeto Date
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							java.util.Date nuevaFechaNacimiento = dateFormat.parse(nuevaFechaNacimientoStr);

							// Preparar la consulta SQL
							String sql = "UPDATE personas SET fecha_nacim = ? WHERE numero = ?";

							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setDate(1, new java.sql.Date(nuevaFechaNacimiento.getTime()));
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizada la fecha de nacimiento");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar. Detalles: " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						System.out.println("No se ha introducido una nueva fecha de nacimiento. La base de datos no se actualizará.");
					}
				}catch(Exception e){
					System.out.println("Error en las variables");
				}
				break;
			}		
	}
	
	private static void modifyCliente(ManejadorBaseDatos manejador) 
		throws ClassNotFoundException, SQLException 
	{
		int opcion= 0; 
		int codigo =0;

			System.out.println("Introduce el numero de la persona que quieras modificar");
			try{
				codigo = sc.nextInt();
				sc.nextLine(); // Consumir el carácter de nueva línea pendiente
				}catch(Exception e){
					System.out.println("Error en el tipo de varaible");
				}
			System.out.println("Seleccione los campos que desea modificar:");
			System.out.println("1. Numero de cuenta");
			System.out.println("2. Estado");
			System.out.println("3. Tipo de Cliente");
			try{
				opcion = sc.nextInt();
				sc.nextLine(); // Consumir el carácter de nueva línea pendiente
				}catch(Exception e ){
					System.out.println("Error ");
				}		
			switch (opcion) {
					case 1:
					try{
						System.out.println("Introduce el nuevo Numero de cuenta");
						int nuevoNumeroCuenta= sc.nextInt(); 
						String nuevoCuentaStr = String.valueOf(nuevoNumeroCuenta);
						if (!nuevoCuentaStr.isEmpty()) {
							try {
								String sql = "UPDATE clientes SET nrocuenta = ? WHERE numero = ?";
								try (Connection con = manejador.conection();
									PreparedStatement preparedStatement = con.prepareStatement(sql)) {
									preparedStatement.setString(1, nuevoCuentaStr);
									preparedStatement.setInt(2, codigo);
									preparedStatement.executeUpdate();
									System.out.println("Actualizado el numero de cuenta");
								}
							} catch (Exception e) {
								System.out.println("Error al guardar");
							}
						} else {
							System.out.println("No se ha introducido un nuevo numero de Cuenta. La base de datos no se actualizará.");
						}
					}catch(Exception e){
						System.out.println("Error en las variables");
					}
						
					break;
					case 2:
					try{
						System.out.println("Introduce el nuevo Estado (Activo, Pendiente, Inactivo):");
						String nuevoEstado = sc.nextLine().trim();

						// Definir los estados válidos
						Set<String> estadosValidos = new HashSet<>(Arrays.asList("Activo", "Pendiente", "Inactivo"));

						if (estadosValidos.contains(nuevoEstado)) {
							try {
								String sql = "UPDATE clientes SET estado = ? WHERE numero = ?";
								try (Connection con = manejador.conection();
									PreparedStatement preparedStatement = con.prepareStatement(sql)) {
									preparedStatement.setString(1, nuevoEstado);
									preparedStatement.setInt(2, codigo);
									preparedStatement.executeUpdate();
									System.out.println("Actualizado el estado");
								}
							} catch (Exception e) {
								System.out.println("Error al guardar");
							}
						} else {
							System.out.println("Estado no válido. Debe ser uno de: Activo, Pendiente, Inactivo. La base de datos no se actualizará.");
						}
					}catch(Exception e){
						System.out.println("Error en las variables");
					}
					break;

					case 3:
					try{
							System.out.println("Introduce el tipo de cliente");
						String nuevoTipoCliente= sc.nextLine(); 
						if (!nuevoTipoCliente.isEmpty()) {
							try {
								String sql = "UPDATE clientes SET tipocliente = ? WHERE numero = ?";
								try (Connection con = manejador.conection();
									PreparedStatement preparedStatement = con.prepareStatement(sql)) {
									preparedStatement.setString(1, nuevoTipoCliente);
									preparedStatement.setInt(2, codigo);
									preparedStatement.executeUpdate();
									System.out.println("Actualizado el tipo de cliente");
								}
							} catch (Exception e) {
								System.out.println("Tipo de cliente no aceptado");
							}
						} else {
							System.out.println("No se ha introducido un nuevo tipo de cliente. La base de datos no se actualizará.");
						}
					}catch(Exception e){
						System.out.println("Error de variables");
					}
					break;
			}			
	}
	private static void modifyFuncionario(ManejadorBaseDatos manejador) 
		throws ClassNotFoundException, SQLException 
	{
		int opcion= 0; 
		int codigo =0;
		System.out.println("Introduce el numero del funcionario que quieras modificar");
		try{
			codigo = sc.nextInt();
			sc.nextLine(); // Consumir el carácter de nueva línea pendiente
			}catch(Exception e){
				System.out.println("Error en el tipo de varaible");
			}
			System.out.println("Seleccione los campos que desea modificar:");
			System.out.println("1. Grupo del Cargo del Funcionario");
			System.out.println("2. Codigo del Cargo del Funcionario");
			System.out.println("3. Departamento del Funcionario");
			System.out.println("4. Fecha de Ingreso del Funcionario");
		try{
		opcion = sc.nextInt();
		sc.nextLine(); // Consumir el carácter de nueva línea pendiente
		}catch(Exception e ){
			System.out.println("Error ");
		}
			
			switch (opcion) {
					case 1:
					try{
					System.out.println("Introduce el grupo del cargo");
					String nuevoGrupo= sc.nextLine(); 
					String nuevoGrupoStr = String.valueOf(nuevoGrupo);
					if (!nuevoGrupoStr.isEmpty()) {
						try {
							String sql = "UPDATE datoCompuesto SET grupo = ? WHERE numero = ?";
							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setString(1, nuevoGrupoStr);
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizado el cargo");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar");
						}
					} else {
						System.out.println("No se ha introducido un nuevo cargo. La base de datos no se actualizará.");
					}
				}catch(Exception e){
					System.out.println("Error de variables");
				}
						
					break;
					case 2:
					try{
					System.out.println("Introduce el codigo del cargo");
					String nuevoCodigo= sc.nextLine(); 
					String nuevoCodigoStr = String.valueOf(nuevoCodigo);
					if (!nuevoCodigoStr.isEmpty()) {
						try {
							String sql = "UPDATE datoCompuesto SET codigo = ? WHERE numero = ?";
							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setString(1, nuevoCodigoStr);
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizado el cargo");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar");
						}
					} else {
						System.out.println("No se ha introducido un nuevo codigo. La base de datos no se actualizará.");
					}
				}catch(Exception e){
					System.out.println("Error de variables");
				}
					break;

					case 3:
						try{
					System.out.println("Introduce el departemento correspondiente: ");
					String nuevoDepartamento= sc.nextLine(); 
					String nuevoDepartamentoStr = String.valueOf(nuevoDepartamento);
					if (!nuevoDepartamentoStr.isEmpty()) {
						try {
							String sql = "UPDATE funcionarios SET departamento = ? WHERE numero = ?";
							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setString(1, nuevoDepartamentoStr);
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizado el cargo");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar");
						}
					} else {
						System.out.println("No se ha introducido un nuevo cargo. La base de datos no se actualizará.");
					}
				}catch(Exception e){
					System.out.println("Error de variables");
				}
				break;
				case 4:
				try{
					System.out.println("Introduce la nueva fecha de ingreso (Formato: dd/MM/yyyy): ");
					String nuevaFechaIngresoStr = sc.nextLine();

					// Verificar si nuevaFechaNacimientoStr no está vacía antes de realizar alguna acción
					if (!nuevaFechaIngresoStr.isEmpty()) {
						try {
							// Convertir la cadena de fecha a un objeto Date
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							java.util.Date nuevaFechaIngreso = dateFormat.parse(nuevaFechaIngresoStr);

							// Preparar la consulta SQL
							String sql = "UPDATE funcionarios SET fecha_ingreso = ? WHERE numero = ?";

							try (Connection con = manejador.conection();
								PreparedStatement preparedStatement = con.prepareStatement(sql)) {
								preparedStatement.setDate(1, new java.sql.Date(nuevaFechaIngreso.getTime()));
								preparedStatement.setInt(2, codigo);
								preparedStatement.executeUpdate();
								System.out.println("Actualizada la fecha de ingreso");
							}
						} catch (Exception e) {
							System.out.println("Error al guardar. Detalles: " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						System.out.println("No se ha introducido una nueva fecha de ingreso. La base de datos no se actualizará.");
					}
				}catch(Exception e){
					System.out.println("Error en la varaible");
				}
				break;
			}		

	}
	
	private static void showPersonas(ManejadorBaseDatos manejador) 
		throws SQLException, ClassNotFoundException 
	{
		  String sql = "SELECT * FROM personas";

        try (Connection con = manejador.conection();
             PreparedStatement preparedStatement = con.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int numero = resultSet.getInt("numero");
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                String fechaNacimiento = resultSet.getString("fecha_nacim");

				 System.out.println("------------------------");
                System.out.println("Número: " + numero);
                System.out.println("Nombre: " + nombre);
                System.out.println("Apellidos: " + apellidos);
                System.out.println("Dirección: " + direccion);
                System.out.println("Teléfono: " + telefono);
                System.out.println("Fecha de Nacimiento: " + fechaNacimiento);
                System.out.println("------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private static void showClientes(ManejadorBaseDatos manejador) 
		throws ClassNotFoundException, SQLException 
	{
		String sql= "SELECT * FROM clientes" ;
		try(Connection con = manejador.conection();
		PreparedStatement preparedStatement = con.prepareStatement(sql);
		ResultSet resultSet= preparedStatement.executeQuery()){ 
			while(resultSet.next()){
				String nmrCuentaString= resultSet.getString("nrocuenta");
				String estado= resultSet.getString("estado");
				String tipoCliente= resultSet.getString("tipocliente");

				System.out.println("------------------------");
				System.out.println("Numero de Cuenta: " + nmrCuentaString);
				System.out.println("Estado: " + estado);
				System.out.println("Tipo de cliente: "+ tipoCliente);
				System.out.println("------------------------");
				if ("Pendiente".equals(estado)) {
					System.out.println("La cuenta bancaria está pendiente de aprobación.");
					System.out.println("------------------------");
				}

			}
		}

	}
	private static void showFuncionarios(ManejadorBaseDatos manejador) 
		throws ClassNotFoundException, SQLException 
	{
		String sql = "SELECT Funcionarios.*, datoCompuesto.grupo, datoCompuesto.codigo " +
                "FROM Funcionarios, datoCompuesto " +
                "WHERE Funcionarios.NUMERO = datoCompuesto.NUMERO";

	   try (Connection con = manejador.conection();
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        ResultSet resultSet = preparedStatement.executeQuery()) {
	
	       while (resultSet.next()) {
	           String departamentoString = resultSet.getString("departamento");
	           String fechaString = resultSet.getString("fecha_ingreso");
	           String grupoStrign = resultSet.getString("grupo");
	           String codigString = resultSet.getString("codigo");
	
	           System.out.println("Departamento: " + departamentoString);
	           System.out.println("Fecha de Ingreso: " + fechaString);
	           System.out.println("Grupo: " + grupoStrign);
	           System.out.println("Codigo: " + codigString);
	       }
		}

	}
}
//cosas que quedan por hacer: corregir los datoscompuestos, mostrar los datos compuestos y editarlos y aparte quiero que en clientes cuando los edite solo se pueda poner tres opciones