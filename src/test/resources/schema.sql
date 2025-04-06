
-- Add custom ST_DISTANCE_SPHERE implementation
CREATE ALIAS IF NOT EXISTS ST_DISTANCE_SPHERE FOR "np.sthaniya.dpis.util.H2GISDistance.distanceSphere";

-- Add custom POINT implementation
CREATE ALIAS IF NOT EXISTS POINT FOR "np.sthaniya.dpis.util.H2GISGeometry.point";