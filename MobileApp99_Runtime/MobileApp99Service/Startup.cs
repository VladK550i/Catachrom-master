using Microsoft.Owin;
using Owin;

[assembly: OwinStartup(typeof(MobileApp99Service.Startup))]

namespace MobileApp99Service
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureMobileApp(app);
        }
    }
}