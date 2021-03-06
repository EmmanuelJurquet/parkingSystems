package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Scanner;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    private static TicketDAO ticketDAO;
    
    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        dataBasePrepareService.clearDataBaseEntries();
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void  testParkingACar(){
    	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	
    	parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actually saved in DB and Parking table is updated with availability
    	Ticket ticket = ticketDAO.getTicket("ABCDEF");
    	assertNotNull(ticket);
    	assertEquals(ticket.getVehicleRegNumber(), "ABCDEF");
    	assertNotNull(ticket.getInTime());
    	assertNull(ticket.getOutTime());
    	assertEquals(ticket.getPrice(), 0);
    	
    	//test :  vehicleRegNumber = "ABCDEF"
    	//test : Date d'entr??e = v??rifier que cela ne soit pas nul
    	//test : date sortie = v??rifier que cela soit nul
    	//test : prix = 0
    	
    	
  
        
    }

    @Test
    public void testParkingLotExit(){
        
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNull (ticket.getOutTime());
        assertEquals (ticket.getPrice(),0);
        
        parkingService.processExitingVehicle();    
        //TODO: check that the fare generated and out time are populated correctly in the database

    }

}
