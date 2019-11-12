/***********************************************************************************************************
** Nombre :   Joaquin Montero. 2DAM. DI --> T2A4                                                        ****
** Ejercicio : AppAgenda ( Agenda con DB apache derbie                                                  ****
** Fuente : https://javiergarciaescobedo.es/programacion-en-java/9-bases-de-datos                       ****
**          pdf ( Desde página 26 hasta final de PDF )                                                  ****
************************************************************************************************************/
package appagenda;
//Clase encargada de realizar consultas

import entidades.Provincia;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class ConsultaProvincias 
{
    public static void main(String[] args) 
    {
        // Lista todas las provincias de la base de datos
        Map<String, String> emfProperties = new HashMap<>();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();
        
        Query queryProvincias = em.createNamedQuery("Provincia.findAll");
        List<Provincia> listProvincias = queryProvincias.getResultList();
        
        for(Provincia provincia : listProvincias)
        {
            System.out.println(provincia.getNombre());
        }
        
        // Agrega el codigo CA a la provincia de Cadiz y la muestra
        Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
        queryProvinciaCadiz.setParameter("nombre", "Cadiz");
        
        List<Provincia> listProvinciasCadiz = queryProvinciaCadiz.getResultList();
       
/*      
        em.getTransaction().begin();
        for(Provincia provinciaCadiz : listProvinciasCadiz) 
        {
            provinciaCadiz.setCodigo("CA");
            em.merge(provinciaCadiz);
        }
        em.getTransaction().commit();
*/
        
        for(Provincia provinciaCadiz : listProvinciasCadiz) 
        {
            System.out.print(provinciaCadiz.getId() + ": ");
            System.out.println(provinciaCadiz.getNombre());
        }
        
        Provincia provinciaId2 = em.find(Provincia.class, 2);
        if(provinciaId2 != null) 
        {
            System.out.print(provinciaId2.getId() + ": ");
            System.out.println(provinciaId2.getNombre());
        }
        else 
        {
            System.out.println("No hay ninguna provincia con ID=2");
        }
        
        // Elimina la provincia numero 15 si existe
        Provincia provinciaId15 = em.find(Provincia.class, 15);
        em.getTransaction().begin();
        if(provinciaId15 != null) 
        {
            em.remove(provinciaId15);
        }
        else 
        {
            System.out.println("No hay ninguna provincia con ID=15");
        }
        em.getTransaction().commit();
        
        //Cierra la conexion
        em.close();
        emf.close();
        try
        {
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        }
        catch(SQLException ex){}
    }  
}
