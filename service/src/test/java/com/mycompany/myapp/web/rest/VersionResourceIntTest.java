package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServiceApp;
import com.mycompany.myapp.domain.Version;
import com.mycompany.myapp.repository.VersionRepository;
import com.mycompany.myapp.service.VersionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VersionResource REST controller.
 *
 * @see VersionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceApp.class)
@WebAppConfiguration
@IntegrationTest
public class VersionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private VersionRepository versionRepository;

    @Inject
    private VersionService versionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVersionMockMvc;

    private Version version;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VersionResource versionResource = new VersionResource();
        ReflectionTestUtils.setField(versionResource, "versionService", versionService);
        this.restVersionMockMvc = MockMvcBuilders.standaloneSetup(versionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        version = new Version();
        version.setName(DEFAULT_NAME);
        version.setDescription(DEFAULT_DESCRIPTION);
        version.setReleaseDate(DEFAULT_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void createVersion() throws Exception {
        int databaseSizeBeforeCreate = versionRepository.findAll().size();

        // Create the Version

        restVersionMockMvc.perform(post("/api/versions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(version)))
                .andExpect(status().isCreated());

        // Validate the Version in the database
        List<Version> versions = versionRepository.findAll();
        assertThat(versions).hasSize(databaseSizeBeforeCreate + 1);
        Version testVersion = versions.get(versions.size() - 1);
        assertThat(testVersion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVersion.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setName(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(version)))
                .andExpect(status().isBadRequest());

        List<Version> versions = versionRepository.findAll();
        assertThat(versions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReleaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionRepository.findAll().size();
        // set the field null
        version.setReleaseDate(null);

        // Create the Version, which fails.

        restVersionMockMvc.perform(post("/api/versions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(version)))
                .andExpect(status().isBadRequest());

        List<Version> versions = versionRepository.findAll();
        assertThat(versions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVersions() throws Exception {
        // Initialize the database
        versionRepository.saveAndFlush(version);

        // Get all the versions
        restVersionMockMvc.perform(get("/api/versions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(version.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getVersion() throws Exception {
        // Initialize the database
        versionRepository.saveAndFlush(version);

        // Get the version
        restVersionMockMvc.perform(get("/api/versions/{id}", version.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(version.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVersion() throws Exception {
        // Get the version
        restVersionMockMvc.perform(get("/api/versions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVersion() throws Exception {
        // Initialize the database
        versionService.save(version);

        int databaseSizeBeforeUpdate = versionRepository.findAll().size();

        // Update the version
        Version updatedVersion = new Version();
        updatedVersion.setId(version.getId());
        updatedVersion.setName(UPDATED_NAME);
        updatedVersion.setDescription(UPDATED_DESCRIPTION);
        updatedVersion.setReleaseDate(UPDATED_RELEASE_DATE);

        restVersionMockMvc.perform(put("/api/versions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVersion)))
                .andExpect(status().isOk());

        // Validate the Version in the database
        List<Version> versions = versionRepository.findAll();
        assertThat(versions).hasSize(databaseSizeBeforeUpdate);
        Version testVersion = versions.get(versions.size() - 1);
        assertThat(testVersion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVersion.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void deleteVersion() throws Exception {
        // Initialize the database
        versionService.save(version);

        int databaseSizeBeforeDelete = versionRepository.findAll().size();

        // Get the version
        restVersionMockMvc.perform(delete("/api/versions/{id}", version.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Version> versions = versionRepository.findAll();
        assertThat(versions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
