const menuItems = {
  items: [
    {
      id: 'website',
      title: 'Website',
      type: 'group',
      icon: 'icon-navigation',
      children: [
        {
          id: 'dashboard',
          title: 'Dashboard',
          type: 'item',
          icon: 'feather icon-home',
          url: '/app/dashboard/default'
        },
        {
          id: 'user-main',
          title: 'Estações',
          type: 'item',
          icon: 'feather icon-map-pin',
          url: '/user-main'
        }
      ]
    }
  ]
};

export default menuItems;