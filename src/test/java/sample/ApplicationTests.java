package sample;

import static org.assertj.core.api.StrictAssertions.assertThat;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sample.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {

	@Autowired
	FilterChainProxy springSecurityFilterChain;

	MockHttpServletRequest request;
	MockHttpServletResponse response;
	MockFilterChain chain;

	@Before
	public void setup() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		chain = new MockFilterChain();
		request.setMethod("GET");
	}

	@Test
	public void customConfiguerPermitAll() throws Exception {
		request.setPathInfo("/public/something");

		springSecurityFilterChain.doFilter(request, response, chain);

		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
	}

	@Test
	public void customConfiguerFormLogin() throws Exception {
		request.setPathInfo("/requires-authentication");

		springSecurityFilterChain.doFilter(request, response, chain);

		assertThat(response.getRedirectedUrl()).endsWith("/custom");
	}

}
