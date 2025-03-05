package com.pwhs.quickmem.core.di

import com.pwhs.quickmem.data.datasource.FolderRemoteDataSourceImpl
import com.pwhs.quickmem.data.datasource.SearchStudySetBySubjectDataSourceImpl
import com.pwhs.quickmem.data.datasource.StudySetRemoteDataSourceImpl
import com.pwhs.quickmem.data.datasource.UserRemoteDataSourceImpl
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.FolderRemoteDataSource
import com.pwhs.quickmem.domain.datasource.SearchStudySetBySubjectRemoteDataSource
import com.pwhs.quickmem.domain.datasource.StudySetRemoteDataSource
import com.pwhs.quickmem.domain.datasource.UserRemoteDataResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideStudySetRemoteDataSource(
        apiService: ApiService
    ): StudySetRemoteDataSource {
        return StudySetRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideFolderRemoteDataSource(
        apiService: ApiService
    ): FolderRemoteDataSource {
        return FolderRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun userRemoteDataSource(
        apiService: ApiService
    ): UserRemoteDataResource {
        return UserRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSearchStudySetBySubjectRemoteDataSource(
        apiService: ApiService
    ): SearchStudySetBySubjectRemoteDataSource {
        return SearchStudySetBySubjectDataSourceImpl(apiService)
    }
}