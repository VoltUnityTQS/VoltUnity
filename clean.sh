#!/bin/bash

echo "🔸 Stopping and removing all containers and volumes..."
docker-compose down -v

echo "🔸 Pruning Docker system (cache, old images, volumes)..."
docker system prune -af --volumes

echo "🔸 Deleting frontend build folders..."
rm -rf frontend/dist
rm -rf frontend/build

echo "🔸 Deleting node_modules (force rebuild)..."
rm -rf frontend/node_modules
rm -rf frontend/package-lock.json # if using npm
# rm -rf frontend/yarn.lock # if using yarn

echo "✅ Clean complete."
echo "➡️ Now run: docker-compose up --build"