package com.smoothstack.alinefinancial.GeneratorTests;

import com.smoothstack.alinefinancial.Maps.CardMap;
import com.smoothstack.alinefinancial.Maps.MerchantMap;
import com.smoothstack.alinefinancial.Maps.StateMap;
import com.smoothstack.alinefinancial.Maps.UserMap;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(
        properties = {
                "input.path = C:\\Users\\Sam\\Documents\\GitHub\\aline-batch-microservice-SS\\alinefinancialbatch\\src\\main\\FilesToProcess\\test2.csv",
                "output.path = C:\\Users\\Sam\\Documents\\GitHub\\aline-batch-microservice-SS\\alinefinancialbatch\\src\\test\\ProcessedOutTestFiles\\"
        })

public class GenerateTests {

    private final UserMap userCache = UserMap.getInstance();
    private final CardMap cardCache = CardMap.getInstance();
    private final MerchantMap merchantCache = MerchantMap.getInstance();
    private final StateMap stateCache = StateMap.getInstance();

    @Test
    public void correctUserGenerationTest(){
        assertTrue(userCache.getGeneratedUsers().isEmpty());
        assertSame(userCache.findUserOrGenerate(0L).getClass(), User.class);
        assertEquals(1, userCache.getGeneratedUsers().size());
        User userOrGenerate = userCache.findUserOrGenerate(0L);
        assertEquals(userOrGenerate.getFirstName(), userCache.getGeneratedUser(0L).getFirstName());
        userCache.getGeneratedUsers().remove(0L);
        assertTrue(userCache.getGeneratedUsers().isEmpty());
    }

    @Test
    public void correctCardGenerationTest(){
        System.out.println(cardCache.getGeneratedCards());
        assertTrue(cardCache.getGeneratedCards().isEmpty());
        cardCache.findOrGenerateCard(0L, 2L);
        assertEquals(4, cardCache.getGeneratedUserCards(0L).size());
        cardCache.getGeneratedCards().remove(0L);
        assertTrue(cardCache.getGeneratedCards().isEmpty());
    }

    @Test
    public void correctMerchantGenerationTest(){
        Transaction t = new Transaction();
        t.setMerchant_name("24654651651651");
        t.setMerchant_state("FL");
        t.setMerchant_city("Gainesville");
        t.setMerchant_zip("32603");
        t.setMcc("1234");
        assertTrue(merchantCache.getGeneratedMerchants().isEmpty());
        assertSame(merchantCache.findMerchantOrGenerate(1L, t.getMerchant_name(), t.getMcc(), t.getAmount()).getClass(), Merchant.class);
        assertEquals(1, merchantCache.getGeneratedMerchants().size());
        Merchant merchant = merchantCache.findMerchantOrGenerate(1L, t.getMerchant_name(), t.getMcc(), t.getAmount());
        assertEquals(merchant.getName(), merchantCache.getGeneratedMerchants().get(t.getMerchant_name()).getName());
        merchantCache.getGeneratedMerchants().remove(t.getMerchant_name());
        assertTrue(merchantCache.getGeneratedMerchants().isEmpty());
    }

    @Test
    public void correctStateTest(){
        Transaction t = new Transaction();
        t.setMerchant_name("24654651651651");
        t.setMerchant_state("FL");
        t.setMerchant_city("Gainesville");
        t.setMerchant_zip("32603");
        t.setMcc("1234");
        assertTrue(merchantCache.getGeneratedMerchants().isEmpty());
        assertTrue(stateCache.getSeenStates().isEmpty());
        stateCache.addSeenStatesAndZip(t);
        assertSame(merchantCache.findMerchantOrGenerate(1L, t.getMerchant_name(), t.getMcc(), t.getAmount()).getClass(), Merchant.class);
        assertEquals(1, merchantCache.getGeneratedMerchants().size());
        assertEquals(1, stateCache.getSeenStates().size());
        stateCache.getSeenStates().remove(t.getMerchant_state());
        merchantCache.getGeneratedMerchants().remove(t.getMerchant_name());
        assertTrue(merchantCache.getGeneratedMerchants().isEmpty());
        assertTrue(stateCache.getSeenStates().isEmpty());
    }
}