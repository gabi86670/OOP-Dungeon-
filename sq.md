```mermaid
sequenceDiagram
    title Receive Alerts and Notifications

    actor Customer
    participant DataWebApp as Financial Data Web-application
    participant DataBackend as Financial data backend API
    participant DataDB as Financial Data Database
    participant NotificationSys as Notification System
    participant DataSources as Data Sources

    Customer ->>+ DataWebApp: [customer has account] Set alert criteria for asset (asset, criteria)
    activate DataWebApp
    DataWebApp ->>+ DataBackend: Customer asset criteria 
    activate DataBackend
    DataBackend -->+ DataDB: Store customer's asset criteria
    activate DataDB
    DataDB -->>- DataBackend: Criteria successfully stored
    deactivate DataBackend
    DataBackend -->> DataWebApp: Confirmation criteria successfully stored
    deactivate DataBackend
    DataWebApp -->>- Customer: Confirmation criteria successfully stored
    deactivate DataWebApp

    DataBackend ->>+ DataSources: [authorisation to access data sources ]Ingest latest price timeseries
    activate DataBackend
    activate DataSources
    DataSources -->>- DataBackend: Updated price timeseries
    deactivate DataSources

    DataBackend ->>+ DataDB: Store updated and cleaned timeseries data
    activate DataDB
    DataDB -->>- DataBackend: Stored successfully
    deactivate DataDB

    DataBackend ->>+ DataDB: Retrieve criteria
    activate DataDB
    DataDB -->>- DataBackend: Customer's criteria
    deactivate DataDB

    DataBackend ->> DataBackend: Evaluate criteria
    alt Alert criteria met
        DataBackend ->>+ NotificationSys: Trigger alert notification
        activate NotificationSys
        NotificationSys -->>- Customer: Notification/Alert
        deactivate NotificationSys
    else No alert triggered
        DataBackend -->>- Customer: no notification (silent)
    end

