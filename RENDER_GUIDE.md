# Render Deployment Guide

To ensure your services communicate correctly via the public internet (using Eureka), you must forcefully verify the `RENDER_EXTERNAL_HOSTNAME` environment variable.

## Step 1: Add Environment Variable in Render
For **each** service (Account, Beneficiary, Gateway, Utility, Subsystem), specificy the following environment variable in the Render Dashboard (under "Environment" > "Environment Variables"):

- **Key:** `RENDER_EXTERNAL_HOSTNAME`
- **Value:** <your-service-url-without-https> (e.g., `account-service.onrender.com`, `bank-gateway.onrender.com`)

**Note:** Render typically adds `RENDER_EXTERNAL_HOSTNAME` automatically for native environments, but for Docker runtimes, you might need to add it manually if the auto-injection fails.

## Step 2: Verification
Check your Eureka Server dashboard (`https://horizon-api-y326.onrender.com`).
You should see services registered as:
- `ACCOUNTSERVICE` -> `account-service.onrender.com:80`
- `EBANKINGSUBSYSTEM` -> `ebanking-sub.onrender.com:80`

If you see `srv-xxxxx` or `localhost` or port `8000/4444` in the link (hover over the status), then the configuration is still incorrect.

## Step 3: Re-Deploy
After adding the variable, re-deploy the services.
