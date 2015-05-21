package persistence;

public class JpaTest {
//    private static final String PERSISTENCE_UNIT_NAME = "auction";
//    private EntityManagerFactory factory;
//
//    @Before
//    public void setUp() throws Exception {
//        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//        EntityManager em = factory.createEntityManager();
//
//        // Begin a new local transaction so that we can persist a new entity
//        em.getTransaction().begin();
//
//        // read the existing entries
//        Query q = em.createQuery("select m from User m");
//        // Persons should be empty
//
//        // do we have entries?
//        boolean createNewEntries = (q.getResultList().size() == 0);
//
//        // No, so lets create new entries
//        if (createNewEntries) {
//            assertTrue(q.getResultList().size() == 0);
//            User user = new User();
//            user.setFirstName("test");
//            user.setLastName("test");
//            user.setPassword("test");
//
//            em.persist(user);
//            for (int i = 0; i < 40; i++) {
//                Lot lot = new Lot();
//                lot.setName("Lot_" + i);
//                lot.setDescription("Knopf_" + i);
//                em.persist(lot);
//                // now persists the family person relationship
//                user.getLots().add(lot);
//                em.persist(lot);
//                em.persist(user);
//            }
//        }
//
//        // Commit the transaction, which will cause the entity to
//        // be stored in the database
//        em.getTransaction().commit();
//
//        // It is always good practice to close the EntityManager so that
//        // resources are conserved.
//        em.close();
//
//    }
//
//    @Test
//    public void checkAvailablePeople() {
//
//        // now lets check the database and see if the created entries are there
//        // create a fresh, new EntityManager
//        EntityManager em = factory.createEntityManager();
//
//        // Perform a simple query for all the Message entities
//        Query q = em.createQuery("select m from User m");
//
//        // We should have 40 Persons in the database
//        assertTrue(q.getResultList().size() == 40);
//
//        em.close();
//    }
//
//    @Test
//    public void checkFamily() {
//        EntityManager em = factory.createEntityManager();
//        // Go through each of the entities and print out each of their
//        // messages, as well as the date on which it was created
//        Query q = em.createQuery("select f from User f");
//
//        // We should have one family with 40 persons
//        assertTrue(q.getResultList().size() == 1);
//        assertTrue(((User) q.getSingleResult()).getLots().size() == 40);
//        em.close();
//    }
//
//    @Test(expected = javax.persistence.NoResultException.class)
//    public void deletePerson() {
//        EntityManager em = factory.createEntityManager();
//        // Begin a new local transaction so that we can persist a new entity
//        em.getTransaction().begin();
//        Query q = em
//                .createQuery("SELECT p FROM Lot p WHERE p.name = :firstName AND p.description = :lastName");
//        q.setParameter("firstName", "Lot_1");
//        q.setParameter("lastName", "Knopf_!");
//        Lot lot = (Lot) q.getSingleResult();
//        em.remove(lot);
//        em.getTransaction().commit();
//        Lot person = (Lot) q.getSingleResult();
//        // Begin a new local transaction so that we can persist a new entity
//
//        em.close();
//    }
}
